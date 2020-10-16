package org.panacea.drmp.nag.controller;


import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc


public class APIPostNotifyDataTest {

//    private static final String NOTIFY_DATA_ENDPOINT = "/nag/notify/data";
//
//
//    @Autowired
//    private WebApplicationContext webApplicationContext;
//
//    private MockMvc mockMvc;
//
//    @Before
//    public void setUp() {
//        this.mockMvc = MockMvcBuilders
//                .webAppContextSetup(webApplicationContext)
//                .build();
//
//
//    }
//
//    private ResultActions performPostNotifyData (String body) throws Exception {
//        return mockMvc.perform(post(NOTIFY_DATA_ENDPOINT)
//                .contentType(MediaType.APPLICATION_JSON_UTF8).content(body)
//                .accept(MediaType.APPLICATION_JSON))
//                .andDo(print());
//    }
//
//    @Test
//    public void whenValidRequest_then200Returned() throws Exception {
//        final String REQ_JSON_TEMPLATE = IOUtils.toString(this.getClass().getClassLoader().getResource("orchestrator/dataNotification.json"));
//        MvcResult result = performPostNotifyData(REQ_JSON_TEMPLATE).andExpect(status().isOk()).andReturn();
//        String responseJson = result.getResponse().getContentAsString();
//        assertTrue(responseJson.contains("environment"));
//    }
//
//    @Test
//    public void whenWRONGRequest_then400Returned() throws Exception {
//        final String REQ_JSON_TEMPLATE_WRONG = IOUtils.toString(this.getClass().getClassLoader().getResource("orchestrator/dataNotification_wrong.json"));
//        MvcResult result = performPostNotifyData(REQ_JSON_TEMPLATE_WRONG).andExpect(status().is4xxClientError()).andReturn();
//        String responseJson = result.getResponse().getContentAsString();
//    }


}
