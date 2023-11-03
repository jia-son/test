package com.connectiontest.test.test;
import java.util.ArrayList;
import java.util.List;

class Solution {
    public int[] solution(int n, String[] words) {
        int[] answer = new int[2];
        List<String> list = new ArrayList<>();
        boolean flag = true;

        for(int i = 0; i < words.length; i++) {
            char one = 0;
            char two = 0;
            if(i > 0) {
                one = words[i-1].charAt(words[i - 1].length() - 1);
                two = words[i].charAt(0);
            }
            if(one != two || list.contains(words[i])) {
                answer[0] = (i%n) + 1;
                answer[1] = (i/n) + 1;
                flag = false;
                break;
            }
            list.add(words[i]);
        }

        if(flag) {
            return new int[]{0, 0};
        }

        return answer;
    }
}