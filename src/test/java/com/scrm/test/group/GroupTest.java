package com.scrm.test.group;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;

import com.scrm.test.BaseDocTest;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class GroupTest extends BaseDocTest {
	 
	// 定时任务执行，变更已到期的活动 timeout
	@Test
	public void timeout() throws Exception {
		this.webTestClient.get().uri("/activity-group/timeout")
				.exchange().expectStatus().isOk().expectBody()
				.consumeWith(WebTestClientRestDocumentation.document("group-service-timeout"));

	}

}
