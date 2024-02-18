package it.spaghettisource.exp.utils;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


/**
 * Utility for Json mapper
 * 
 * 
 * @author Alessandro D'Ottavio
 * @version 1.0
 */
public class JsonConverter {

	
	/**
	 * Build the mapper and import and load the appropriate modules.
	 * In the pom i already defined all the depdences to load the extra modules if it will be needed for the:
	 *  - ParameterNamesModule
	 *  - Jdk8Module 
	 *  
	 *  but in this moment the app just load the JavaTimeModule. 
	 *  
	 *  NOTE: 
	 *  The application doesn't need the extra model, but in the code below i already wrote the code to loading it.
	 *  This code for now is commented, but just uncomment it if in future we will need it
	 * 
	 * @return
	 */
	private static ObjectMapper buildMapper() {
		return JsonMapper.builder()
						//.addModule(new ParameterNamesModule())
				   		//.addModule(new Jdk8Module())
				   		.addModule(new JavaTimeModule())
				   		.build();
		
	}
	

	/**
	 * Convert a java bean to a json
	 * 
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public static String objectToJson(Object object) throws Exception	 {

		ObjectMapper mapper = buildMapper(); 

		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false); 	//null properties will not generate and error

		try { 
			// covert the object as a json string 
			String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object); 
			return jsonStr;
		} catch (JsonProcessingException cause) { 
			throw new Exception(cause);
		} 

	}


	/**
	 * map the values in the json to a java bean
	 * 
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public static <T> T jsonToObject(String json, Class<T> target) throws Exception {

		ObjectMapper mapper = buildMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);  	//properties present in the json but not in the bean will not generate an error
		//mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
		
		try { 
			// covert the object as a json string 
			T object = mapper.readValue(json, target); 
			return object;
		} catch (JsonProcessingException cause) { 
			throw new Exception(cause);
		} 

	}

	
	/**
	 *  map the values in the json to a java bean using a specific deserializer
	 * 
	 * @param json
	 * @param target
	 * @param deserialized
	 * @param deserializedClass
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public static <T> T jsonToObject(String json, Class<T> target,StdDeserializer<?> deserialized,Class deserializedClass) throws Exception {

		ObjectMapper mapper = buildMapper();
  
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);  	//properties present in the json but not in the bean will not generate an error
		
		SimpleModule module =new SimpleModule("CustomDeserializer", new Version(1, 0, 0, null, null, null));
		module.addDeserializer(deserializedClass, deserialized);
		mapper.registerModule(module);		

		try { 
			// covert the object as a json string 
			T object = mapper.readValue(json, target); 
			return object;
		} catch (JsonProcessingException cause) { 
			throw new Exception(cause);
		} 

	}
	
	
	/**
	 * serialize the Json that represent a list of object to a list
	 * the json must be valid or this method return an  exception
	 * 
	 * in case the json represent an empty list: [] the method return List object empty
	 * 
	 * @param exceptionFactory
	 * @param json to parse
	 * @param tClass to the type of object to serialize
	 * @return a List of object of type tClass
	 * @throws Exception 
	 */
	public static <T> List<T> jsonToObjectList(String json, Class<T> tClass) throws Exception{

		ObjectMapper mapper = buildMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);  	//properties present in the json but not in the bean will not generate an error
		
		CollectionType listType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, tClass);
		
		try { 		
			List<T> object = mapper.readValue(json, listType);
			return object;
		}catch (JsonProcessingException cause) { 
			throw new Exception(cause);
		} 
	}	


}
