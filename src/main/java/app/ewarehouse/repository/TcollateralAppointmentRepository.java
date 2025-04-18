package app.ewarehouse.repository;
import app.ewarehouse.entity.TcollateralAppointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TcollateralAppointmentRepository extends JpaRepository <TcollateralAppointment, Integer>{


}
