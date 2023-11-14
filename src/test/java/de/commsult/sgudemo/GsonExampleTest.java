package de.commsult.sgudemo;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GsonExampleTest {

    GsonExample tester;

    @BeforeEach
    void setup() {
        tester = new GsonExample();
        tester.dao = EasyMock.createMock(DatabaseDao.class);
    }

    @Test
    void createPerson() {
        Person person = tester.createPerson("Tanis", "Geraldi", 26);

        Assertions.assertNotNull(person);
        Assertions.assertEquals("Tanis", person.getName());
        Assertions.assertEquals("Geraldi", person.getFirstname());
        Assertions.assertEquals(26, person.getAge());
    }

    @Test
    void makeJsonOfPerson() {
        Person person = new Person();
        person.setName("Schenke");
        person.setFirstname("Tobias");
        person.setAge(44);

        String json = tester.makeJsonOfPerson(person);

        Assertions.assertNotNull(json);
        Assertions.assertEquals("{\"name\":\"Schenke\",\"firstname\":\"Tobias\",\"age\":44}", json);
    }

    @Test
    void doSaveToDatabase() {

        Person person = new Person();
        person.setName("Schenke");
        person.setFirstname("Tobias");
        person.setAge(44);

        EasyMock.expect(tester.dao.save("{\"name\":\"Schenke\",\"firstname\":\"Tobias\",\"age\":44}")).andReturn(true);

        EasyMock.replay(tester.dao);
        boolean isSaved = tester.doSaveToDatabase(person);
        EasyMock.verify(tester.dao);

        Assertions.assertTrue(isSaved);

    }

    @Test
    void doSaveToDatabaseYoungPerson() {

        Person person = new Person();
        person.setName("Schenke");
        person.setFirstname("Tobias");
        person.setAge(28);

        EasyMock.expect(tester.dao.save("{\"name\":\"Schenke\",\"firstname\":\"Tobias\",\"age\":28}")).andReturn(true);

        EasyMock.replay(tester.dao);
        boolean isSaved = tester.doSaveToDatabase(person);
        EasyMock.verify(tester.dao);

        Assertions.assertTrue(isSaved);

    }
}
