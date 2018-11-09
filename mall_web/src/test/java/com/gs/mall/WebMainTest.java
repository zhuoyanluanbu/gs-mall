//package com.gs.mall;
//
///**
// * Created by Administrator on 2017/9/18.
// */
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.HttpMethod;
//import org.springframework.mock.web.MockHttpServletResponse;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.net.URI;
//
///**
// * Created by huangyp on 2017/9/15.
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@ContextConfiguration(classes = WebMain.class)
//@Transactional
//public class WebMainTest {
//
//    @Autowired
//    private WebApplicationContext wac;
//
//    private MockMvc mockMvc;
//
//    @Before
//    public void setup() {
//        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
//    }
//
//    @Test
//    public void testOrderController() throws Exception {
//        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, URI.create("/web/detail")).param("id", "2"));
//        MvcResult rst = action.andReturn();
//        MockHttpServletResponse resp = rst.getResponse();
//        if (resp.getStatus() == 200) {
//            String str = resp.getContentAsString();
//            System.out.println(str);
//        } else {
//            throw new IllegalStateException("response status : " + resp.getStatus());
//        }
//    }
//
//
//    @After
//    public void destory() {
//        mockMvc = null;
//        wac = null;
//    }
//}
