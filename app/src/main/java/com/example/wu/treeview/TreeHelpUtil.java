package com.example.wu.treeview;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wu on 2016/6/6.
 */
public class TreeHelpUtil {
    private static List<Object> datas;

    /**
     * 把数据转变为node
     *
     * @param <T>
     * @return
     */
    private static <T> List<Node> convertDatas2Nodes(List<T> datas) throws IllegalAccessException {
        List<Node> nodes = new ArrayList<Node>();
        for (T data : datas) {
            int id = -1;
            int pId = -1;
            String level = null;
            Class clazz = data.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {

                if (field.getAnnotation(TreeNodeId.class) != null) {
                    field.setAccessible(true);
                    id = field.getInt(data);
                }
                if (field.getAnnotation(TreeNodePId.class) != null) {
                    field.setAccessible(true);
                    pId = field.getInt(data);
                }
                if (field.getAnnotation(TreeNodeLebel.class) != null) {
                    field.setAccessible(true);
                    level = (String) field.get(data);
                }
            }
            Node node = new Node(id, pId, level);
            nodes.add(node);
        }
        /**
         * 设置节点间的相互关系
         */
        int len = nodes.size();
        for (int i = 0; i < len; i++) {
            Node n = nodes.get(i);
            for (int j = i + 1; j < len; j++) {
                Node m = nodes.get(j);
                if (m.getpId() == n.getId()) {
                    n.getChildren().add(m);
                    m.setParent(n);
                } else if (n.getpId() == m.getId()) {
                    m.getChildren().add(n);
                    n.setParent(m);
                }
            }
        }


        for (Node n : nodes) {
            setNodeIcon(n);
        }
        return nodes;
    }

    /**
     * 排序数据并返回list数据
     * @param datas
     * @param defaultExpandLevel
     * @param <T>
     * @return
     * @throws IllegalAccessException
     */
    public static <T> List<Node> getStortedNodes(List<T> datas, int defaultExpandLevel) throws IllegalAccessException {
        List<Node> result = new ArrayList<Node>();

        List<Node> nodes = convertDatas2Nodes(datas);

        List<Node> roots = getRootNodes(nodes);
        for (Node node : roots) {
            addNode(result, node, defaultExpandLevel, 1);
        }
        return result;
    }


    public static  List<Node> filterVisiableNode(List<Node> nodes){
        List<Node> result=new ArrayList<Node>();
        for(Node node:nodes){
            if(node.isRoot() || node.isParentExpand()){
                setNodeIcon(node);
                result.add(node);
            }
        }
        return result;
    }

    /**
     * 将子节点增加到节点
     *
     * @param result
     * @param node
     * @param defaultExpandLevel
     * @param currentLevel
     */
    private static void addNode(List<Node> result, Node node, int defaultExpandLevel, int currentLevel) {
        result.add(node);
        if (defaultExpandLevel >= currentLevel) {
            node.setExpand(true);
        } else {
            node.setExpand(false);
        }
        if(node.isLeaf()){
            return;
        }
        for (int i=0;i<node.getChildren().size();i++){
            addNode(node.getChildren(),node,defaultExpandLevel,currentLevel+1);
        }
    }

    /**
     * 获取根节点
     *
     * @param nodes
     * @return
     */
    private static <T> List<Node> getRootNodes(List<Node> nodes) {
        List<Node> root = new ArrayList<Node>();
        for (Node node : nodes) {
            if (node.isRoot()) {
                root.add(node);
            }
        }
        return root;
    }


    /**
     * 设置图标
     *
     * @param node
     */
    private static void setNodeIcon(Node node) {
        if (node.getChildren().size() > 0 && node.isExpand()) {
            node.setIcon(R.drawable.down);
        } else if (node.getChildren().size() > 0 && !node.isExpand()) {
            node.setIcon(R.drawable.right);
        } else {
            node.setIcon(-1);
        }
    }
}
