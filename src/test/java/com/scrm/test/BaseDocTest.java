package com.scrm.test;

import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;

import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.netty.resources.LoopResources;
import reactor.netty.tcp.TcpClient;

public class BaseDocTest {
	@Rule
	public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("src/test/snippets");
	@Autowired
	protected ObjectMapper objectMapper;
	protected MockMvc mockMvc;

	protected WebTestClient webTestClient;
	@Autowired
	ApplicationContext context;

	protected String scheme = "http";
	protected String host = "127.0.0.1";
	protected Integer port = 81;
	protected String webBaseUrl = "http://127.0.0.1:81";
	protected String userServiceContextPath = "/user-service";
	protected String contextPath = "user-service";
	protected String token = "d28973a9-2b2d-4908-a633-48c37080b3f6";

	protected FieldDescriptor[] dataField = new FieldDescriptor[] {};
	protected FieldDescriptor[] respField = new FieldDescriptor[] {};

	protected FieldDescriptor[] pageFields = new FieldDescriptor[] {};

	@Before
	public void setUp() {

		final ConnectionProvider theTcpClientPool = ConnectionProvider.elastic("tcp-client-pool");
		final LoopResources theTcpClientLoopResources = LoopResources.create("tcp-client-loop");

		final TcpClient theTcpClient = TcpClient
		    .create(theTcpClientPool)
		    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 50000)// 用来设置连接超时时长,单位是毫秒
		    .runOn(theTcpClientLoopResources)
		    .doOnConnected(theConnection -> {
		        theConnection.addHandlerLast(new ReadTimeoutHandler(10000, TimeUnit.MILLISECONDS));// 客户端获取读到信息的时间
		        theConnection.addHandlerLast(new WriteTimeoutHandler(10000, TimeUnit.MILLISECONDS));// 远程将信息写入客户端完成的时间
		    });
		ClientHttpConnector httpConnector = new ReactorClientHttpConnector(HttpClient.from(theTcpClient));

		this.webTestClient = WebTestClient.bindToServer(httpConnector)
				.filter(documentationConfiguration(restDocumentation)).baseUrl(webBaseUrl).build();
	}

	protected ResponseFieldsSnippet commonResponseFields(FieldDescriptor... fieldDescriptors) {
		List<FieldDescriptor> fieldDescriptorList = new ArrayList<FieldDescriptor>();
		fieldDescriptorList.add(fieldWithPath("code").type(Integer.class.getSimpleName()).description("状态码"));
		fieldDescriptorList.add(fieldWithPath("status").type(JsonFieldType.STRING).description("状态说明"));
		fieldDescriptorList.add(fieldWithPath("msg").type(JsonFieldType.STRING).description("提示消息"));
		if (fieldDescriptors != null) {
			fieldDescriptorList.addAll(Arrays.asList(fieldDescriptors));
		}
		return responseFields(fieldDescriptorList);
	}

	protected FieldDescriptor fieldWithPath(String path) {
		return PayloadDocumentation.fieldWithPath(path);
	}
}
