package es.ucm.fdi.iw.Repositories;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.ucm.fdi.iw.model.Report;


public interface ReportRepository extends JpaRepository<Report,Long> {
    @Query(value = "SELECT * FROM Report r " + 
    "WHERE r.user_Target = :userId", nativeQuery = true)  
    ArrayList<Report> findReportsByUserId(@Param("userId") long userId);
}