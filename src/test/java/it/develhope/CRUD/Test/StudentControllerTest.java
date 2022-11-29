package it.develhope.CRUD.Test;

import it.develhope.CRUD.Test.controllers.StudentController;
import it.develhope.CRUD.Test.entities.Student;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import java.util.List;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@ActiveProfiles(value = "test")
@AutoConfigureMockMvc
class StudentControllerTest {
	@Autowired
	private StudentController studentController;
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void studentControllerLoads() {
		assertThat(studentController);
	}

	private Student getStudentFromId(Long id) throws Exception{
		MvcResult result = this.mockMvc.perform(get("/student/" + id))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		try {
			String studentJSON = result.getResponse().getContentAsString();
			Student student = objectMapper.readValue(studentJSON, Student.class);

			assertThat(student).isNotNull();
			assertThat(student.getId()).isNotNull();

			return student;
		}catch (Exception e){
			return null;
		}
	}

	private Student createAStudent() throws Exception {
		Student student = new Student();
		student.setWorking(true);
		student.setName("Emi");
		student.setSurname("Fag");

		return createAStudent(student);
	}

	private Student createAStudent(Student student) throws Exception {
		MvcResult result = createAStudentRequest(student);
		Student studentFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), Student.class);
		return studentFromResponse;
	}

	//Creo una funzione ausiliaria che serve per creare uno studente e vedere se funziona
	private MvcResult createAStudentRequest() throws Exception {
		Student student = new Student();
		student.setWorking(true);
		student.setName("Emi");
		student.setSurname("Fag");

		return createAStudentRequest(student);
	}

	private MvcResult createAStudentRequest(Student student) throws Exception {
		if(student == null)return null;
		String studentJSON = objectMapper.writeValueAsString(student);
		//Fammi il post di student
		return this.mockMvc.perform(post("/student")
						//Gli diciamo che è un JSON
						.contentType(MediaType.APPLICATION_JSON)
						//gli diamo il contenuto
						.content(studentJSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
	}


	@Test
	void createAStudentTest() throws Exception {
		//Creiamo lo studente e controlliamo che non sia nullo
		Student studentFromResponse = createAStudent();
		assertThat(studentFromResponse.getId()).isNotNull();
	}

	@Test
	void readStudentList() throws Exception{
		createAStudent();

		MvcResult result= this.mockMvc.perform(get("/student"))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		List<Student> studentFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), List.class);
		System.out.println("Student in database are: "+ studentFromResponse.size());
		assertThat(studentFromResponse.size()).isNotZero();
	}

	@Test
	void readSingleStudent()throws Exception {
		Student student = createAStudent();
		Student studentFromResponse = getStudentFromId(student.getId());
		assertThat(studentFromResponse.getId()).isEqualTo(student.getId());

	}

	@Test
	void updateStudent()throws Exception{
		//Creiamo lo studente
		Student student = createAStudent();

		//Diamo un nuovo nome
		String newName= "Giovanni";
		student.setName(newName);
		//Andiamo a trasferire l'oggetto student in una stringa studentJSON
		String studentJSON = objectMapper.writeValueAsString(student);

		MvcResult result = this.mockMvc.perform(put("/student/"+ student.getId())
						//Gli diciamo che è un JSON
						.contentType(MediaType.APPLICATION_JSON)
						//gli diamo il contenuto
						.content(studentJSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		//Controlliamo che lo studente che abbiamo letto abbia lo stesso id di quello che abbiamo creato
		Student studentFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), Student.class);

		assertThat(studentFromResponse.getId()).isEqualTo(student.getId());
		assertThat(studentFromResponse.getName()).isEqualTo(newName);

		Student studentFromResponseGet = getStudentFromId(student.getId());
		assertThat(studentFromResponseGet.getId()).isEqualTo(student.getId());
		assertThat(studentFromResponseGet.getName()).isEqualTo(newName);
	}

	@Test
	void deleteStudent()throws Exception{
		Student student = createAStudent();
		assertThat(student.getId()).isNotNull();

		MvcResult result = this.mockMvc.perform(delete("/student/"+ student.getId()))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		Student studentFromResponseGet = getStudentFromId(student.getId());
		assertThat(studentFromResponseGet).isNull();

	}

	@Test
	void activateStudent()throws Exception{
		Student student = createAStudent();
		assertThat(student.getId()).isNotNull();

		MvcResult result = this.mockMvc.perform(put("/student/"+ student.getId()+"/work?working=true"))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		Student studentFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), Student.class);
		assertThat(studentFromResponse).isNotNull();
		assertThat(studentFromResponse.getId()).isEqualTo(student.getId());
		assertThat(studentFromResponse.isWorking()).isEqualTo(true);

		Student studentFromResponseGet = getStudentFromId(student.getId());
		assertThat(studentFromResponseGet).isNotNull();
		assertThat(studentFromResponseGet.getId()).isEqualTo(student.getId());
		assertThat(studentFromResponseGet.isWorking()).isEqualTo(true);

	}


}