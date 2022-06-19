package testRest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="user")  // anotação necessária para o teste de salvar usuário com XML
@XmlAccessorType(XmlAccessType.FIELD) // pega todos atributos da classe e GETS não cobertos
public class User { //classe de serialização do ojeto JSON

    @XmlAttribute
    private Long id;
    private String name;
    private Integer age;
    private Double salary;

    public User () {
        // método sem argumento necessário para o teste de salvar usuário com XML
    }

    public User(String name, Integer age) {  // construtor para os atributos "name" e "age"
        super();
        this.name = name;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {  // criado o método para na forma da conversão
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", salary=" + salary +
                '}';
    }
}
