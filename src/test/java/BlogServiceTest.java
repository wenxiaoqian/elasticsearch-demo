import com.welab.boot.entity.Blog;
import com.welab.boot.service.BlogService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by WXQ on 2016/10/14.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class BlogServiceTest {

    @Autowired
    private BlogService blogService;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Before
    public void before(){
        elasticsearchTemplate.deleteIndex(Blog.class);
        elasticsearchTemplate.createIndex(Blog.class);
        elasticsearchTemplate.putMapping(Blog.class);
        elasticsearchTemplate.refresh(Blog.class);
    }


    @Test
    public void testSave(){
        Blog blog = new Blog();
        blog.setId("12");
        blog.setTitle("Welab产品介绍");
        blog.setContent("Welab产品介绍");
        blog.setPosttime(LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE));
        blogService.save(blog);
    }

    @Test
    public void testfindByTitle(){

        Blog blog = new Blog();
        blog.setId("12");
        blog.setTitle("Welab产品介绍");
        blog.setContent("Welab产品介绍");
        blog.setPosttime(LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE));
        blogService.save(blog);

        Page<Blog> blogs = blogService.findByTitle("Welab产品介绍",new PageRequest(0, 10));
        assertThat(blogs.getTotalElements(), is(1L));
    }

    @Test
    public void testFindOne(){
        Blog blog = new Blog();
        blog.setId("12");
        blog.setTitle("Welab产品介绍");
        blog.setContent("Welab产品介绍");
        blog.setPosttime(LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE));
        blogService.save(blog);

        Blog vblog = blogService.findOne("12");
        assertEquals(vblog.getTitle(), "Welab产品介绍");
    }
}
