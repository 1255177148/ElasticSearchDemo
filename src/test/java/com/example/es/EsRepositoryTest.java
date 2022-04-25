package com.example.es;

import com.alibaba.fastjson.JSON;
import com.example.es.dao.DemoRepository;
import com.example.es.entity.Demo;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
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
     * 保存文档，没有则新增，有则更新
     */
    @Test
    void save() {
        Demo demo = new Demo();
        String id = UUID.randomUUID().toString();
        demo.setId(id);
        demo.setAge(8);
        demo.setName("这TM是八岁?");
        demo.setDesc("这TM是八岁?!");
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

    /**
     * 批量保存文档，没有则新增，有则更新
     */
    @Test
    void saveAll() {
        List<Demo> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Demo demo = new Demo();
            demo.setId(UUID.randomUUID().toString());
            demo.setAge(i + 1);
            demo.setName("测试一下" + i);
            demo.setDesc("测试一下" + i);
            list.add(demo);
        }
        demoRepository.saveAll(list);
    }

    /**
     * 获取指定index的所有文档，分页+排序
     */
    @Test
    void getAllByPage() {
        Sort sort = Sort.by(Sort.Direction.DESC, "age");
        Pageable page = PageRequest.of(0, 20, sort);
        Page<Demo> demos = demoRepository.findAll(page);
        System.out.println(JSON.toJSONString(demos));
    }

    /**
     * 获取指定index的所有文档
     */
    @Test
    void getAll() {
        Iterable<Demo> list = demoRepository.findAll();
        List<Demo> demos = Lists.newArrayList(list);
        System.out.println(JSON.toJSONString(demos));
    }
}
