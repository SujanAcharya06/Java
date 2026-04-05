


## SpringBootRest JPA

```java
// Controller
@GetMapping("jobPost/keyword/{keyword}")
@ResponseBody
public List<JobPost> searchKeyword(@PathVariable String keyword) {
	return jobService.searchKeywod(keyword);
}

// Service
public List<JobPost> searchKeywod(String keyword) {
	return jobRepo.findByPostProfileContainingOrPostDescContaining(keyword, keyword);
}

//repo
@Repository
public interface JobRepo extends JpaRepository<JobPost, Integer> {

	List<JobPost> findByPostProfileContainingOrPostDescContaining(String postDesc, String postProfile);

}
```
