package com.pizhiyong.dailypractice;

import com.pizhiyong.dailypractice.util.RandomUtils;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

@SpringBootTest
public class DailyPracticeApplicationTests {

    @Test
    public void contextLoads() {
        Set<String> set = new HashSet<>();
        for (int i = 0; i < 1000; i++) {
            String s = RandomUtils.genDspId("1");
            System.out.println(s);
            set.add(s);
        }
        System.out.println(set.size() == 1000);
    }

}
