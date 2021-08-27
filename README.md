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
4. DTO 직접 조회
```
@Query( " select new study.datajpa.repository.MemberDto(m.id, m.username, t.name) from Member m join m.team t)
List<MemberDto> findMemberDto();
```
5. 컬렉션 파라미터 바인딩
```
@Query (" select m from Member m where m.username in :names")
List<Member> findByNames(@Param("names") List<String>names);
```
6. 페이징과 정렬
```
Page<Member> findByUsername(String name, Pageble pageble); // 카운터 쿼리 사용
Slice<Member> findByUsername(String name, Pageble pageble); // 카운터 쿼리 사용 x
List<Member> findByUsername(String name, Sort sort); 
```
> page유지하면서 dto 변경 가능 Page<Member> page = memberRepository(10, pageRequest);
> Page<MemberDto> dtoPage = page.map(MemberDto::New);

            
7. 벌크성 쿼리
```
int resultCount = em.createQuery( "update Member m set m.age= m.age +1 where m.age >= :age")
            .setParameter("age", age)
            .executeUpdate();

@Modifying
@Query(" update Member m set m.age = m.age +1 where m.age >= : age")
int bulkAgePlus(@Param int age)
```
8. 엔티티 그래프, 패치 조인
```
@Query("select m from Member m left join fetch m.team")
List<Member> findMemberFetchJoin();

@Override
@EntityGraph(attributePaths= {"team"})
List<Member> findAll();
```

9. QueryHint
```
@QueryHints( value = @QueryHint(name= "org.hibernate.readOnly", value = "true))
Member findReadOnlyByUsername(String useranme);
```
10. 사용자 정의 인터페이스 구현
```
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom

public interface MemberRepositoryCustom {
    List<Member> findMemberCustom();
}
public class MemberRepositoryImpl implements MemberRepositoryCustom            
```
> 사용자가 정의한 곳에 메소드를 만들면 된다.
       
          
        
            
      
            


