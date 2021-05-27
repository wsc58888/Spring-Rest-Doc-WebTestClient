package com.scrm.test.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.BodyInserters;

import com.scrm.test.BaseDocTest;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class SysUserTest extends BaseDocTest {
	private FieldDescriptor[] respTokenField = new FieldDescriptor[] {
			fieldWithPath("token").type(String.class.getSimpleName()).optional().description("登录返回Token"), };
	private FieldDescriptor[] responseUserField = new FieldDescriptor[] {
			fieldWithPath("id").type(Long.class.getSimpleName()).optional().description("ID"),
			fieldWithPath("userId").type(String.class.getSimpleName()).optional().description("用户账号"),
			fieldWithPath("name").type(String.class.getSimpleName()).optional().description("用户名称"),
			fieldWithPath("deptId").type(Long.class.getSimpleName()).optional().description("部门ID"),
			fieldWithPath("deptName").type(String.class.getSimpleName()).optional().description("部门名称"),
			fieldWithPath("mobile").type(String.class.getSimpleName()).optional().description("手机号码"),
			fieldWithPath("createTime").type(String.class.getSimpleName()).optional().description("创建时间"),
			fieldWithPath("updateTime").type(String.class.getSimpleName()).optional().description("更新时间"),
			fieldWithPath("lastLoginTime").type(String.class.getSimpleName()).optional().description("最后登录时间"),
			fieldWithPath("remark").type(String.class.getSimpleName()).optional().description("备注"),
			fieldWithPath("roleName").type(String.class.getSimpleName()).optional().description("角色名称"),
			fieldWithPath("storeId").type(String.class.getSimpleName()).optional().description("门店ID"),
			fieldWithPath("storeName").type(String.class.getSimpleName()).optional().description("门店名称"),
			fieldWithPath("storeVos").type(String.class.getSimpleName()).optional().description(""),
			fieldWithPath("status").type(String.class.getSimpleName()).optional().description("状态"),

	};
	private FieldDescriptor[] responseUserRoleField = new FieldDescriptor[] {
			fieldWithPath("roleId").type(Long.class.getSimpleName()).optional().description("角色ID"),
			fieldWithPath("roleName").type(String.class.getSimpleName()).optional().description("平台管理员"),
			fieldWithPath("type").type(Integer.class.getSimpleName()).optional().description("类型:0.平台管理员;1:运营人员,2:财务人员")

	};
	private FieldDescriptor[] responseUserRoleMenuField = new FieldDescriptor[] {
			fieldWithPath("id").type(Long.class.getSimpleName()).optional().description("菜单ID"),
			fieldWithPath("name").type(String.class.getSimpleName()).optional().description("菜单名称"),
			fieldWithPath("icon").type(String.class.getSimpleName()).optional().description("图标"),
			fieldWithPath("orderNum").type(String.class.getSimpleName()).optional().description("排序"),
			fieldWithPath("url").type(String.class.getSimpleName()).optional().description("链接路径"),
			fieldWithPath("parentId").type(Integer.class.getSimpleName()).optional().description("父菜单ID"),
			fieldWithPath("createTime").type(String.class.getSimpleName()).optional().description("创建时间"),
			fieldWithPath("status").type(Integer.class.getSimpleName()).optional().description("状态"),
			fieldWithPath("children").type(Integer.class.getSimpleName()).optional().description("子菜单"),

	};

	// login
	@Test
	public void login() throws Exception {
		String requestJson="{\"userId\":\"wangsencai\",\n" + 
				" \"password\":\"WSCwsc123\"\n" + 
				"}";
		this.webTestClient.post().uri("/user-service/sysUser/login").body(BodyInserters.fromValue(requestJson))
				.header("Content-Type", "application/json")
				.exchange().expectStatus().isOk().expectBody()
				.consumeWith(WebTestClientRestDocumentation.document("user-service-login", // 生成adoc文档所在文件夹名称
						PayloadDocumentation.relaxedRequestFields(fieldWithPath("userId").description("用户名"),
								fieldWithPath("password").description("用户密码")),
						commonResponseFields(dataField).andWithPrefix("body.", respTokenField)
								.andWithPrefix("body.user.", responseUserField)
								.andWithPrefix("body.user.userRoleVos[]", responseUserRoleField)
								.andWithPrefix("body.user.userRoleVos[].menuList[].", responseUserRoleMenuField)
								.andWithPrefix("body.user.userRoleVos[].menuList[].children[].",
										responseUserRoleMenuField)

				));

	}

}
