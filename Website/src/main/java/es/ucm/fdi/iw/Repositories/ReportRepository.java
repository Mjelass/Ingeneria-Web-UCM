package es.ucm.fdi.iw.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import es.ucm.fdi.iw.model.Report;


public interface ReportRepository extends JpaRepository<Report,Long> {

}