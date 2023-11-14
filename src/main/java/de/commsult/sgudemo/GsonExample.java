package de.commsult.sgudemo;

import com.google.gson.Gson;

public class GsonExample {

    DatabaseDao dao = null;

    public static void main(String[] args) {

        GsonExample ex = new GsonExample();
        Person person1 = ex.createPerson("Schenke", "Tobias", 44);

        String json = ex.makeJsonOfPerson(person1);

        System.out.println(json);

    }

    protected Person createPerson(String name, String firstName, int age) {
        Person p = new Person();
        p.setName(name);
        p.setFirstname(firstName);
        p.setAge(26);

        return p;
    }

    protected String makeJsonOfPerson(Person person) {
        String json = new Gson().toJson(person);

        return json;
    }

    protected boolean doSaveToDatabase(Person person) {

        if (person.getAge() > 40) {
            System.out.println("hello world");
        }
        String json = makeJsonOfPerson(person);

        boolean save = dao.save(json);

        return save;
    }

}
