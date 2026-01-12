package com.sernova.domain;

import com.sernova.dto.PersonAddressDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    @Query("""
        select new com.sernova.dto.PersonAddressDto(
            p.id,
            p.firstName,
            p.lastName,
            a.id,
            a.line1,
            a.city,
            a.country,
            a.type
        )
        from Person p
        left join p.addresses a
    """)
    Page<PersonAddressDto> findPersonsWithAddresses(Pageable pageable);
}
