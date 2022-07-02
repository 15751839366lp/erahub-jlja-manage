package com.erahub.asset.metadata.utils;

import com.erahub.common.model.asset.metadata.AssetCategory;
import com.erahub.common.utils.ArithmeticUtils;
import com.erahub.common.vo.asset.metadata.AssetCategoryVO;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Author lipeng
 * @Date 2022/4/17 9:24
 * @Version 1.0
 **/
public class AssetCategoryTreeBuilder {

    /**
     * 构建多级树
     * @param nodes
     * @return
     */
    public static List<AssetCategoryVO> build(List<AssetCategory> nodes){

        LinkedHashMap<Long, LinkedHashMap<Long, AssetCategoryVO>> tmpMap = new LinkedHashMap<>();

        nodes.forEach(item -> {
            long length = item.getAssetCategoryId().length();

            AssetCategoryVO assetCategoryVo = new AssetCategoryVO();
            BeanUtils.copyProperties(item, assetCategoryVo);

            if (tmpMap.containsKey(length)) {
                tmpMap.get(length).put(Long.valueOf(item.getAssetCategoryId()), assetCategoryVo);
            } else {
                LinkedHashMap<Long, AssetCategoryVO> tmpList = new LinkedHashMap<>();
                tmpList.put(Long.valueOf(item.getAssetCategoryId()), assetCategoryVo);
                tmpMap.put(length, tmpList);
            }
        });


        ListIterator<Map.Entry> listIterator = new ArrayList<Map.Entry>(tmpMap.entrySet()).listIterator(tmpMap.size());
        LinkedHashMap<Long, AssetCategoryVO> nextMap = new LinkedHashMap<>();
        LinkedHashMap<Long, AssetCategoryVO> previousMap = new LinkedHashMap<>();
        while (listIterator.hasPrevious()) {
            Map.Entry tmpEntry = listIterator.previous();
            previousMap = (LinkedHashMap<Long, AssetCategoryVO>) tmpEntry.getValue();
            if (nextMap.size() != 0) {
                for(Map.Entry<Long, AssetCategoryVO> entrySet : nextMap.entrySet()) {
                    Long prevNode = Long.valueOf(entrySet.getKey().toString().substring(0, entrySet.getKey().toString().length() - 2));
                    List<AssetCategoryVO> children = previousMap.get(prevNode).getChildren();
                    if(children != null){
                        children.add(entrySet.getValue());
                    }else {
                        List<AssetCategoryVO> child = new ArrayList<>();
                        child.add(entrySet.getValue());
                        previousMap.get(prevNode).setChildren(child);
                    }
                }
            }
            nextMap = previousMap;
        }
        LinkedHashMap<Long, AssetCategoryVO> rs = (LinkedHashMap<Long, AssetCategoryVO>) tmpMap.values().toArray()[0];
        List result = new ArrayList(rs.values());
        return result;
    }
}
