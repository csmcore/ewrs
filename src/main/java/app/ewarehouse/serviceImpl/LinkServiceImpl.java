package app.ewarehouse.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.ewarehouse.dto.LinkDTO;
import app.ewarehouse.dto.LinkOrderDto;
import app.ewarehouse.entity.Tmenulinks;
import app.ewarehouse.repository.TmenulinksRepository;
import app.ewarehouse.service.LinkService;
import app.ewarehouse.util.CommonUtil;

@Service
public class LinkServiceImpl implements LinkService {
	private static final Logger logger = LoggerFactory.getLogger(LinkServiceImpl.class);
	private final TmenulinksRepository repo;
    private int globalIndex = 1;
    private final ObjectMapper om;
    
    public LinkServiceImpl(TmenulinksRepository repo,ObjectMapper om) {
        this.repo = repo;
        this.om = om;
    }
    
    @Override
    public JSONObject processLinks(String data) {
    	logger.info("inside processLiks method of LinkServiceImpl");
        String decodedData = CommonUtil.inputStreamDecoder(data);
        JSONObject json = new JSONObject();
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            LinkOrderDto listData = om.readValue(decodedData, LinkOrderDto.class);
            List<LinkDTO> orders = listData.getOrder();

            List<Tmenulinks> menusToSave = new ArrayList<>();
            List<Tmenulinks> submenusToSave = new ArrayList<>();

            for (LinkDTO order : orders) {
                Tmenulinks menu = repo.findByIntIdAndBitDeletedFlag(order.getIntId(), false);
                if (menu != null) {
                    menu.setTxtSerialNo(globalIndex++);
                    menusToSave.add(menu);  // Collect for batch update
                }

                if (order.getIntLinkType() == 1 && order.getSubcategory() != null) {
                    for (LinkDTO sub : order.getSubcategory()) {
                        Tmenulinks submenu = repo.findByIntIdAndBitDeletedFlag(sub.getIntId(), false);
                        if (submenu != null) {
                            submenu.setTxtSerialNo(globalIndex++);
                            submenusToSave.add(submenu);
                        }
                    }
                }
            }

            if (!menusToSave.isEmpty()) {
                repo.saveAll(menusToSave);
            }
            if (!submenusToSave.isEmpty()) {
                repo.saveAll(submenusToSave);
            }

            json.put("status", 200);
            json.put("result", "Data Saved Successfully");

        } catch (Exception e) {
            json.put("status", 500);
            json.put("result", "Error : " + e.getMessage());
        }

        return json;
    }

    
//    @Override
//    public JSONObject processLinks(String data) {
//    	String decodedData = CommonUtil.inputStreamDecoder(data);
//		JSONObject json = new JSONObject();
//		ObjectMapper om = new ObjectMapper();
//		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//		try {
//			LinkOrderDto listData = om.readValue(decodedData,
//					LinkOrderDto.class);
//			List<LinkDTO> x = listData.getOrder();
//			for(LinkDTO y : x) {
//				Tmenulinks menu = repo.findByIntIdAndBitDeletedFlag(y.getIntId(), false);
//				if(menu != null) {
//					menu.setTxtSerialNo(globalIndex++);
//					repo.save(menu);
//				}
//				if(y.getIntLinkType() == 1 && y.getSubcategory() != null) {
//					for(LinkDTO z : y.getSubcategory()) {
//						Tmenulinks submenu = repo.findByIntIdAndBitDeletedFlag(z.getIntId(), false);
//						if(submenu != null) {
//							submenu.setTxtSerialNo(globalIndex++);
//							repo.save(submenu);
//						}
//					}
//				}
//			}
//			json.put("status", 200);
//			json.put("result", "Data Saved Successfully");
//		} catch (Exception e) {
//			json.put("status", 200);
//			json.put("result", "Error : "+e.getMessage());
//		}		
//        return json;
//    }
}
