package com.erahub.fixedasset.metadata.utils;

import com.erahub.common.model.fixedasset.metadata.FixedAssetCategory;
import com.erahub.common.utils.ArithmeticUtils;
import com.erahub.common.vo.fixedasset.metadata.FixedAssetCategoryVO;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Author lipeng
 * @Date 2022/4/17 9:24
 * @Version 1.0
 **/
public class FixedAssetCategoryTreeBuilder {

    /**
     * 构建多级树
     * @param nodes
     * @return
     */
    public static List<FixedAssetCategoryVO> build(List<FixedAssetCategory> nodes){

        LinkedHashMap<Long, LinkedHashMap<Long, FixedAssetCategoryVO>> tmpMap = new LinkedHashMap<>();

        nodes.forEach(item -> {
            long length = item.getCategoryId().toString().length();

            FixedAssetCategoryVO fixedAssetCategoryVo = new FixedAssetCategoryVO();
            BeanUtils.copyProperties(item, fixedAssetCategoryVo);

            fixedAssetCategoryVo.setDepreciationPeriod(new BigDecimal(ArithmeticUtils.div(item.getDepreciationPeriod(),100,2)));
            fixedAssetCategoryVo.setEstimatedTotalWorkload(new BigDecimal(ArithmeticUtils.div(item.getEstimatedTotalWorkload(),100,2)));
            fixedAssetCategoryVo.setNetResidualValue(new BigDecimal(ArithmeticUtils.div(item.getNetResidualValue(),100,2)));

            if (tmpMap.containsKey(length)) {
                tmpMap.get(length).put(item.getCategoryId(), fixedAssetCategoryVo);
            } else {
                LinkedHashMap<Long, FixedAssetCategoryVO> tmpList = new LinkedHashMap<>();
                tmpList.put(item.getCategoryId(), fixedAssetCategoryVo);
                tmpMap.put(length, tmpList);
            }
        });


        ListIterator<Map.Entry> listIterator = new ArrayList<Map.Entry>(tmpMap.entrySet()).listIterator(tmpMap.size());
        LinkedHashMap<Long, FixedAssetCategoryVO> nextMap = new LinkedHashMap<>();
        LinkedHashMap<Long, FixedAssetCategoryVO> previousMap = new LinkedHashMap<>();
        while (listIterator.hasPrevious()) {
            Map.Entry tmpEntry = listIterator.previous();
            previousMap = (LinkedHashMap<Long, FixedAssetCategoryVO>) tmpEntry.getValue();
            if (nextMap.size() != 0) {
                for(Map.Entry<Long, FixedAssetCategoryVO> entrySet : nextMap.entrySet()) {
                    Long prevNode = Long.valueOf(entrySet.getKey().toString().substring(0, entrySet.getKey().toString().length() - 2));
                    List<FixedAssetCategoryVO> children = previousMap.get(prevNode).getChildren();
                    if(children != null){
                        children.add(entrySet.getValue());
                    }else {
                        List<FixedAssetCategoryVO> child = new ArrayList<>();
                        child.add(entrySet.getValue());
                        previousMap.get(prevNode).setChildren(child);
                    }
                }
            }
            nextMap = previousMap;
        }
        LinkedHashMap<Long, FixedAssetCategoryVO> rs = (LinkedHashMap<Long, FixedAssetCategoryVO>) tmpMap.values().toArray()[0];
        List result = new ArrayList(rs.values());
        return result;
    }
}
