package com.erahub.asset.metadata.utils;

import com.erahub.common.model.asset.metadata.Section;
import com.erahub.common.vo.asset.metadata.SectionVO;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @Author lipeng
 * @Date 2022/4/28 18:15
 * @Version 1.0
 **/
public class SectionTreeBuilder {

    /**
     * 构建多级树
     * @param nodes
     * @return
     */
    public static List<SectionVO> build(List<Section> nodes){

        List<SectionVO> noParentNodes = new ArrayList<>();

        Map<Long, Map<String, SectionVO>> tmpMap = new TreeMap<>(new Comparator<Long>() {
            @Override
            public int compare(Long o1, Long o2) {
                return o1.compareTo(o2);
            }
        });

        //按级分类
        nodes.forEach(item -> {
            long length = item.getSectionId().length();
            SectionVO sectionVO = new SectionVO();
            BeanUtils.copyProperties(item, sectionVO);

            if(item.getAssetQuantity() == null){
                sectionVO.setAssetQuantity(0l);
            }

//            if(item.getSectionDetailed() != null){
//                sectionVO.setHasChildren(item.getSectionDetailed() == 1 ? false : true);
//            }

            if (tmpMap.containsKey(length)) {
                tmpMap.get(length).put(item.getSectionId(), sectionVO);
            } else {
                Map<String, SectionVO> tmpList = new TreeMap<>(new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        return o1.compareTo(o2);
                    }
                });
                tmpList.put(item.getSectionId(), sectionVO);
                tmpMap.put(length, tmpList);
            }
        });

        ListIterator<Map.Entry> listIterator = new ArrayList<Map.Entry>(tmpMap.entrySet()).listIterator(tmpMap.size());
        Map<String, SectionVO> nextMap = new TreeMap<>();
        Map<String, SectionVO> previousMap = new TreeMap<>();
        while (listIterator.hasPrevious()) {
            Map.Entry tmpEntry = listIterator.previous();
            previousMap = (TreeMap<String, SectionVO>) tmpEntry.getValue();
            if (nextMap.size() != 0) {
                for(Map.Entry<String, SectionVO> entrySet : nextMap.entrySet()) {
                    String prevNodeId = entrySet.getKey().substring(0, entrySet.getKey().length() - 4);
                    if(!previousMap.containsKey(prevNodeId)){
                        noParentNodes.add(entrySet.getValue());
                    }else{
                        List<SectionVO> children = previousMap.get(prevNodeId).getChildren();
                        if(!StringUtils.isEmpty(prevNodeId)){
                            if(children != null){
                                children.add(entrySet.getValue());
                            }else {
                                List<SectionVO> child = new ArrayList<>();
                                child.add(entrySet.getValue());
                                previousMap.get(prevNodeId).setChildren(child);
                            }
                        }
                    }
                }
            }
            for(Map.Entry<String, SectionVO> entrySet : previousMap.entrySet()) {
                List<SectionVO> parent = entrySet.getValue().getChildren();
                if(parent != null && parent.size() > 0){
                    Collections.sort(parent, new Comparator<SectionVO>() {
                        @Override
                        public int compare(SectionVO o1, SectionVO o2) {
                            return o1.getSectionId().compareTo(o2.getSectionId());
                        }
                    });
                }
            }
            nextMap = previousMap;
        }
        TreeMap<String, SectionVO> rs = (TreeMap<String, SectionVO>) tmpMap.values().toArray()[0];
        List tree = new ArrayList(rs.values());

        if(noParentNodes.size() != 0){
            SectionVO noParentSection = new SectionVO();
            noParentSection.setSectionId("9999");
            noParentSection.setSectionName("其他单位");
            noParentSection.setSectionAbbreviation("其他");
            noParentSection.setLevel(1l);
            noParentSection.setDetailed(0l);
            noParentSection.setStatus(1l);
            noParentSection.setChildren(noParentNodes);

            tree.add(noParentSection);
        }

        return tree;
    }
}
