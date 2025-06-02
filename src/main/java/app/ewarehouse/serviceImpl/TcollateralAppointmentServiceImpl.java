package app.ewarehouse.serviceImpl;
import app.ewarehouse.entity.ComplaintApplicationStatus;
import app.ewarehouse.entity.Complaint_managment;
import app.ewarehouse.entity.TcollateralAppointment;
import app.ewarehouse.exception.CustomGeneralException;
import app.ewarehouse.repository.Complaint_managmentRepository;
import app.ewarehouse.repository.TcollateralAppointmentRepository;
import app.ewarehouse.service.TcollateralAppointmentService;
import app.ewarehouse.util.CommonUtil;
import app.ewarehouse.util.ErrorMessages;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;


@Service
public class TcollateralAppointmentServiceImpl implements TcollateralAppointmentService {

    @Autowired
    TcollateralAppointmentRepository repo;
    @Autowired
    ErrorMessages errorMessages;
    @Autowired
    TcollateralAppointmentRepository tcollateralAppointmentRepository;
    @Autowired
    Complaint_managmentRepository complaint_managmentRepository;
    @Autowired
    private ObjectMapper om;

    private static final Logger logger = LoggerFactory.getLogger(TcollateralAppointmentServiceImpl.class);

    @Override
    public String save(String formData) throws JsonProcessingException {
        try {
            logger.info("inside TcollateralAppointmentServiceImpl");
            String decodedData = CommonUtil.inputStreamDecoder(formData);
            TcollateralAppointment appointment = om.readValue(decodedData, TcollateralAppointment.class);

            Complaint_managment complaint=complaint_managmentRepository.findByIntIdAndBitDeletedFlag(appointment.getComplaintId(),false);
            if (complaint == null){
                throw new Exception("Complaint does not exist");
            }
            complaint.setApplicationStatus("SOLVED");
            logger.info("inside save method:"+appointment);
            tcollateralAppointmentRepository.save(appointment);
            return appointment.getCollateralManager();
        }catch (DataIntegrityViolationException e){
                logger.error("Data integrity violation: " + e.getMessage());
                throw new CustomGeneralException(errorMessages.getUnknownError());
            }
        catch (JsonProcessingException e) {
        logger.error("Json Processing violation: "+e);
        throw new CustomGeneralException(errorMessages.getUnknownError());
    }
        catch (Exception e){
        logger.error("General exception:"+e);
        throw new CustomGeneralException(errorMessages.getInternalServerError());
    }

    }
}
