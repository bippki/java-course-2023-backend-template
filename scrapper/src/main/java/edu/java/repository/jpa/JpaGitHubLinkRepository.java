package edu.java.repository.jpa;

import edu.java.entity.GithubLink;
import org.springframework.data.repository.CrudRepository;

public interface JpaGitHubLinkRepository extends CrudRepository<GithubLink, Long> {
}
