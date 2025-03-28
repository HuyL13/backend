package com.bluemoonproject.repository;

import com.bluemoonproject.entity.Fee;
import com.bluemoonproject.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeeRepository extends JpaRepository<Fee, Long> {

}//
