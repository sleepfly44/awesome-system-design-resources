package intuit;

import java.util.ArrayList;

public class Intuit {
    static class Company{
        String name;
        ArrayList<Company> subsidiaries;

        public Company(String name, ArrayList<Company> subsidiaries){
            this.name = name;
            this.subsidiaries = subsidiaries;
        }

        public Company(String name){
            this.name = name;
            this.subsidiaries = new ArrayList<>();
        }

        public ArrayList<Company> getSubsidiaries(){
            return this.subsidiaries;
        }

        public String getName(){
            return this.name;
        }

    }

    public int dfs(Company node, String targetName, ArrayList<String> path, int len){
        if(node == null){
            return len;
        }

        path.add(node.getName());

        if(node.getName().equals(targetName)){
            return path.size() - 1; // Found the target, return path length
        }

        for(Company child : node.getSubsidiaries()){
            len = dfs(child, targetName, path, len);
            if(len != -1){
                return len; // Target found in subtree
            }
        }

        path.remove(path.size() - 1); // Backtrack
        return len; // Target not found in this path
    }
    public int getShortestPath(Company root, String targetName){
        ArrayList<String> path = new ArrayList<>();
        int len = -1;
        len = dfs(root, targetName, path, len);
        return len; // Target not found
    }
}
