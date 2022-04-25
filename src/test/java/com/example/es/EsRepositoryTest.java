package com.example.es;

import com.alibaba.fastjson.JSON;
import com.example.es.dao.DemoRepository;
import com.example.es.entity.Demo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.UUID;

/**
 * @Author elvis
 * @Date 2022/4/25 10:23
 */
@SpringBootTest
public class EsRepositoryTest {

    @Resource
    private DemoRepository demoRepository;

    /**
     * 保存文档
     */
    @Test
    void save() {
        Demo demo = new Demo();
        String id = UUID.randomUUID().toString();
        demo.setId(id);
        demo.setAge(8);
        demo.setName("这TM是八岁?");
        demo.setDesc("这TM是八岁!");
        demoRepository.save(demo);
        System.out.println(id);
    }

    /**
     * 根据id获取保存的文档
     */
    @Test
    void getById() {
        String id = "c33cc242-24b0-4c04-9978-38036c5e6170";// 这里的id是上面save()方法里的id
        Optional<Demo> demo = demoRepository.findById(id);
        System.out.println(JSON.toJSONString(demo.orElse(null)));
    }
}
