package leetcode;

class Solution {
    public int jump(int[] nums) {
        int size = nums.length;
        int[] dp = new int[size];
        for(int i = size-2; i>=0; i--){
            if(nums[i]==0){
                dp[i] = 10000;
                continue;
            }
            if((nums[i] + i)>=size-1){
                dp[i] =1;
            }
            else{
                int minSteps = size;
                System.out.println(i);
                for(int j=0;j<nums[i]; j++){
                    minSteps = Math.min(minSteps, dp[i+j+1] + 1);
                    //System.out.println(minSteps);
                }
                dp[i] = minSteps;
            }
        }
        System.out.println(dp[0]);
        return (dp[0]!=10000)?dp[0]:0;
    }

    public static void main(String[] args) {
        Solution s = new Solution();
        int[] arr = {2,3,1,1,4};
        s.jump(arr);
    }
}