package edu.java.repository.jpa;

import edu.java.entity.StackOverflowLink;
import org.springframework.data.repository.CrudRepository;

public interface JpaStackOverflowLinkRepository extends CrudRepository<StackOverflowLink, Long> {
}
