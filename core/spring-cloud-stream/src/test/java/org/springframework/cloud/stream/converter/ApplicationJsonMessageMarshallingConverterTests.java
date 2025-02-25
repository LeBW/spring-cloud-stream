/*
 * Copyright 2021-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.stream.converter;

import java.util.Collections;
import java.util.Map;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.Test;

import org.springframework.core.ResolvableType;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.messaging.support.GenericMessage;

import static org.junit.Assert.fail;

@SuppressWarnings("deprecation")
public class ApplicationJsonMessageMarshallingConverterTests {

	@Test
	void badJson() {

		try {
			ApplicationJsonMessageMarshallingConverter converter = new ApplicationJsonMessageMarshallingConverter(
					initObjectMapper());
			converter.convertFromInternal(
					new GenericMessage<>("{ notjson }".getBytes()), JsonNode.class, null);
			fail();
		}
		catch (MessageConversionException e) {
			// Good
		}
		catch (Throwable t) {
			fail();
		}
	}

	@Test
	void errorPropagationTestOnCollection() {
		ApplicationJsonMessageMarshallingConverter converter = new ApplicationJsonMessageMarshallingConverter(
				JsonMapper.builder().build());

		try {
			converter.fromMessage(new GenericMessage<>(Collections.singletonList("{ \"field1\": 1 }")), Map.class,
					ResolvableType.forClassWithGenerics(Map.class, String.class, String.class).getType());
			fail();
		}
		catch (MessageConversionException e) {
			// good
		}
		catch (Throwable t) {
			fail();
		}
	}

	private ObjectMapper initObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return objectMapper;
	}

}
