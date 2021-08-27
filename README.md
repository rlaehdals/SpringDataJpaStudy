## SpringDataJpaStudy

## spring setting
1. spring boot version 2.2.1
2. java 8

## dependency
1. web
2. jpa
3. lombok
4. h2 database

## 쿼리 메소드 기능

1. 메소드 이름
```
List<Member> findByUsernameAndAgeGraterThan(String username, int age);
```

2. JPA NamedQuery
```
@Entity
@NameQuery(
            name= "Member.findByUseranme",
            query= " select m from Member m where m.username= :username")
public class Member{
   ~~~
}

List<Member> list =em.createNamedQuery("Member.findByUsername", Member.class)
                  .setParameter("username", username)
                  .getResultList();
```
3. @Query
```
@Query( "select m from Member m where m.username= :username and m.age= :age")
List<Member> findUser(@Param("username") String username, @Param("age") int age);
```
4. 


