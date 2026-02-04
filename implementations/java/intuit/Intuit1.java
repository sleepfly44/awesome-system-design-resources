package intuit;


import java.util.*;

public class Intuit1 {
    static class Company {
        String name;
        ArrayList<Company> subsidiaries;

        public Company(String name, ArrayList<Company> subsidiaries) {
            this.name = name;
            this.subsidiaries = subsidiaries;
        }

        public Company(String name) {
            this.name = name;
            this.subsidiaries = new ArrayList<>();
        }

        public ArrayList<Company> getSubsidiaries() {
            return this.subsidiaries;
        }

        public String getName() {
            return this.name;
        }

        public void addSubsidiary(ArrayList<Company> subsidiary) {
            this.subsidiaries = subsidiary;
        }
    }

    public int getShortestPath(Company root, String targetName) {
        if (root == null) {
            return -1; // Return -1 if the root is null
        }
        Set<Company> visited = new HashSet<>();
        Stack<Pair> stack = new Stack<>();
        stack.push(new Pair(root, new ArrayList<>()));

        while (!stack.isEmpty()) {
            Pair current = stack.pop();
            Company node = current.node;
            ArrayList<String> path = current.path;
            if (visited.contains(node)) {
                continue; // Skip already visited nodes
            }
            visited.add(node);
            path.add(node.getName());

            if (node.getName().equals(targetName)) {
                return path.size() - 1; // Found the target, return path length
            }

            for (Company child : node.getSubsidiaries()) {
                ArrayList<String> newPath = new ArrayList<>(path);
                stack.push(new Pair(child, newPath));
            }
        }

        return -1; // Target not found
    }

    static class Pair {
        Company node;
        ArrayList<String> path;

        Pair(Company node, ArrayList<String> path) {
            this.node = node;
            this.path = path;
        }
    }

    public static void main(String[] args) {
        Intuit1 intuit = new Intuit1();


        Company company2 = new Company("Company2");
        Company company3 = new Company("Company3");
        Company company4 = new Company("Company4");
        Company company5 = new Company("Company5");
        Company company6 = new Company("Company6");
        Company company7 = new Company("Company7");
        Company company8 = new Company("Company8");
        Company company9 = new Company("Company9");
        Company company10 = new Company("Company10");
        company2.addSubsidiary(new ArrayList<>(Arrays.asList(company10, company5, company4)));
        company3.addSubsidiary(new ArrayList<>(Arrays.asList(company6, company9, company10)));
        company4.addSubsidiary(new ArrayList<>(Arrays.asList(company2, company5, company4)));
        company5.addSubsidiary(new ArrayList<>(Arrays.asList(company2, company3, company7)));
        // Test the tree
        int pathLength = intuit.getShortestPath(company2, "Company7");
        System.out.println("Shortest path length: " + pathLength);
        int[] arr = {1,2,3,4,5};
        int length = arr.length;

    }
}