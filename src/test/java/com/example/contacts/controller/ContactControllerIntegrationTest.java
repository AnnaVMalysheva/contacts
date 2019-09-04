package com.example.contacts.controller;

import com.example.contacts.ContactApplication;
import com.example.contacts.dto.ContactDto;
import com.example.contacts.dto.SearchContactDto;
import com.example.contacts.entity.Contact;
import com.example.contacts.repository.ContactRepository;
import com.example.contacts.service.ContactService;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static com.example.contacts.TestHelper.*;
import static org.junit.Assert.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ContextConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ContactApplication.class, webEnvironment = RANDOM_PORT)
@PropertySource("classpath:/application.properties")
@Slf4j
public class ContactControllerIntegrationTest {
	private static final ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ContactRepository contactRepository;

	@Autowired
	private ContactService contactService;

	@After
	public void clean(){
		contactRepository.deleteAll();
	}

	@Test
	@WithUserDetails(userDetailsServiceBeanName = "userDetailsService")
	public void addContact() throws Exception {

		MvcResult result = mockMvc.perform(post(BASE_URL)
				.content(objectMapper.writeValueAsString(CONTACT_DTO)).contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isCreated()).andReturn();
		ContactDto addedContactDto = objectMapper.readValue(result.getResponse().getContentAsString(),
				ContactDto.class);

		Contact foundContact = contactRepository.findById(addedContactDto.getId()).orElse(null);

		assertNotNull(foundContact);
		assertEquals(CONTACT_DTO.getFirstName(), foundContact.getFirstName());
		assertEquals(CONTACT_DTO.getLastName(), foundContact.getLastName());
		assertEquals(CONTACT_DTO.getEmail(), foundContact.getEmail());
		assertEquals(CONTACT_DTO.getPhone(), foundContact.getPhone());
	}

	@Test
	@WithUserDetails(userDetailsServiceBeanName = "userDetailsService")
	public void addDuplicateContact() throws Exception {
        contactRepository.save(CONTACT);

		mockMvc.perform(post(BASE_URL).content(objectMapper.writeValueAsString(CONTACT_DTO))
				.contentType(APPLICATION_JSON_UTF8)).andExpect(status().isConflict()).andReturn();
	}

	@Test
	@WithUserDetails(userDetailsServiceBeanName = "userDetailsService")
	public void deleteContact() throws Exception {
		ContactDto contactDto = contactService.add(CONTACT_DTO);

		mockMvc.perform(delete(BASE_URL + "/" + contactDto.getId()).contentType(APPLICATION_JSON_UTF8)).andExpect(status().isOk());

		assertNull(contactRepository.findById(contactDto.getId()).orElse(null));
	}

	@Test
	@WithUserDetails(userDetailsServiceBeanName = "userDetailsService")
	public void deleteContactNotFound() throws Exception {
		mockMvc.perform(delete(BASE_URL + "/1").contentType(APPLICATION_JSON_UTF8)).andExpect(status().isNotFound());
	}


	@Test
	@WithUserDetails(userDetailsServiceBeanName = "userDetailsService")
	public void updateAppRequest() throws Exception {

		MvcResult result = mockMvc.perform(post(BASE_URL).content(objectMapper.writeValueAsString(CONTACT_DTO)).contentType(APPLICATION_JSON_UTF8)).andExpect(status().isCreated()).andReturn();

		ContactDto resultContactDto = objectMapper.readValue(result.getResponse().getContentAsString(), ContactDto.class);
		String updPostfix = "_new";

        ContactDto updatedContactDto = ContactDto.builder()
                .firstName(CONTACT_DTO.getFirstName() + updPostfix)
                .lastName(CONTACT_DTO.getLastName() + updPostfix)
                .email(CONTACT_DTO.getEmail() + updPostfix)
                .phone(CONTACT_DTO.getPhone())
                .build();

		result = mockMvc.perform(put(BASE_URL + "/" + resultContactDto.getId()).content(objectMapper.writeValueAsString(updatedContactDto)).contentType(APPLICATION_JSON_UTF8)).andExpect(status().isOk()).andReturn();

		resultContactDto = objectMapper.readValue(result.getResponse().getContentAsString(), ContactDto.class);

		Contact updatedContact = contactRepository.findById(resultContactDto.getId()).orElse(null);

		assertNotNull(updatedContact);
		assertEquals(updatedContactDto.getFirstName(), updatedContact.getFirstName());
		assertEquals(updatedContactDto.getLastName(), updatedContact.getLastName());
		assertEquals(updatedContactDto.getPhone(), updatedContact.getPhone());
		assertEquals(updatedContactDto.getEmail(), updatedContact.getEmail());
	}

	@Test
	@WithUserDetails(userDetailsServiceBeanName = "userDetailsService")
	public void updateNotFoundContact() throws Exception {

		String updPostfix = "_new";

		ContactDto updatedContactDto = ContactDto.builder()
				.firstName(CONTACT_DTO.getFirstName() + updPostfix)
				.lastName(CONTACT_DTO.getLastName() + updPostfix)
				.email(CONTACT_DTO.getEmail() + updPostfix)
				.phone(CONTACT_DTO.getPhone())
				.build();

		mockMvc.perform(put(BASE_URL + "/1").content(objectMapper.writeValueAsString(updatedContactDto)).contentType(APPLICATION_JSON_UTF8)).andExpect(status().isNotFound()).andReturn();
	}


	@Test
	@WithUserDetails(userDetailsServiceBeanName = "userDetailsService")
	public void searchContact() throws Exception {

		MvcResult result = mockMvc.perform(post(BASE_URL)
				.content(objectMapper.writeValueAsString(CONTACT_DTO)).contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isCreated()).andReturn();

		SearchContactDto searchContactDto = SearchContactDto.builder()
				.firstName(SEARCH_CONTACT_DTO.getFirstName())
				.build();

		MvcResult foundResult = mockMvc.perform(post(BASE_URL + "/search")
				.content(objectMapper.writeValueAsString(searchContactDto)).contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andReturn();

		ContactDto foundContactDto = objectMapper.readValue(result.getResponse().getContentAsString(),
				ContactDto.class);

		assertEquals(CONTACT_DTO.getFirstName(), foundContactDto.getFirstName());
		assertEquals(CONTACT_DTO.getLastName(), foundContactDto.getLastName());
		assertEquals(CONTACT_DTO.getEmail(), foundContactDto.getEmail());
		assertEquals(CONTACT_DTO.getPhone(), foundContactDto.getPhone());
	}

//	@Test(expected = EntityNotFoundException.class)
//	@WithUserDetails(value = "user1", userDetailsServiceBeanName = "managerUserDetailsService")
//	public void deleteAppRequest() throws Exception {
//		AppRequestConfigDto appRequestConfigDto = appService.addAppRequest(APP_REQUEST_CONFIG_DTO, 1L);
//		MultiValueMap params = new LinkedMultiValueMap<>();
//		params.add("id", appRequestConfigDto.getId().toString());
//		mockMvc.perform(delete(BASE_URL).params(params).contentType(APPLICATION_JSON_UTF8)).andExpect(status().isOk());
//		appRequestRepository.findById(appRequestConfigDto.getId()).orElseThrow(EntityNotFoundException::new);
//	}
//
//	@Test
//	@WithUserDetails(value = "user1", userDetailsServiceBeanName = "managerUserDetailsService")
//	public void setState() throws Exception {
//		AppRequestConfigDto appRequestConfigDto = appService.addAppRequest(APP_REQUEST_CONFIG_DTO, 1L);
//		JSONObject appRequestStateDto = new JSONObject();
//		appRequestStateDto.put("id", appRequestConfigDto.getId());
//		appRequestStateDto.put("lastState", IN_REVIEW.toString());
//		mockMvc.perform(put(BASE_URL + "/state").content(appRequestStateDto.toJSONString()).contentType(APPLICATION_JSON_UTF8)).andExpect(status().isOk());
//		AppRequest newRequest = appRequestRepository.findById(appRequestConfigDto.getId()).orElseThrow(EntityNotFoundException::new);
//		assertEquals(IN_REVIEW, newRequest.getLastState());
//	}
//
//	@Test
//	@WithUserDetails(value = "user1", userDetailsServiceBeanName = "managerUserDetailsService")
//	public void setConfig() throws Exception {
//		AppRequestConfigDto appRequestConfigDto = appService.addAppRequest(APP_REQUEST_CONFIG_DTO, 1L);
//		JSONObject config = new JSONObject();
//		config.put("key1", "value1");
//		config.put("key2", "value2");
//		JSONObject request = new JSONObject();
//		request.put("id", appRequestConfigDto.getId());
//		request.put("config", config);
//		mockMvc.perform(put(BASE_URL + "/config").content(request.toJSONString()).contentType(APPLICATION_JSON_UTF8)).andExpect(status().isOk());
//		AppRequest newRequest = appRequestRepository.findById(appRequestConfigDto.getId()).orElseThrow(EntityNotFoundException::new);
//
//		HashMap configMap = new HashMap();
//		configMap.put("key1", "value1");
//		configMap.put("key2", "value2");
//		configMap.put("key3", "value3");
//
//		assertThat(newRequest.getConfig(), is(configMap));
//	}
}