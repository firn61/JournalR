package ru.donenergo.journalr.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.context.WebApplicationContext;
import ru.donenergo.journalr.dao.IHostRestrictionDAO;

@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class HostRestrictionService implements IHostRestrictionService, IDataToModelSetter {

    private final IHostRestrictionDAO hostDAO;
    private String resName;
    private Integer hostResNum;
    private String hostRights;
    private String ipAddress;
    public final String AUTHORIZED_MESSAGE = "Ok!";
    public final String FORBIDDEN_MESSAGE = "Недостаточно прав для внесения изменений!";
    static final Logger logger = LoggerFactory.getLogger(HostRestrictionService.class);

    @Autowired
    public HostRestrictionService(IHostRestrictionDAO hostDAO) {
        this.hostDAO = hostDAO;
    }

    public void invokeValues(String ipAddressReq) {
        if (ipAddress == null) {
            ipAddress = ipAddressReq;
            hostRights = getHostRightsFromDAO(ipAddress);
            resName = hostDAO.getHostCyrResName(ipAddress);
            hostResNum = hostDAO.getHostResNum(ipAddress);
            logger.info("invoked values: resName: {}, hostResNum: {}, hostRights: {}", resName, hostResNum, hostRights);
        }
    }
    @Override
    public void setDataToModel(Model model) {
        logger.info("values added to model: resName: {}, {}", resName, hostResNum);
        model.addAttribute("noRightsMessage", FORBIDDEN_MESSAGE);
        model.addAttribute("hostResNum", hostResNum);
        model.addAttribute("resName", resName);
    }

    @Override
    public String getHostRightsFromDAO(String ipAddress) {
        try {
            logger.info("rights found for: {}", ipAddress);
            return hostDAO.getHostRights(ipAddress);
        } catch (EmptyResultDataAccessException e) {
            logger.warn("no rights found for: {}, default access rights applied", ipAddress);
            hostDAO.addReadOnlyRights(ipAddress);
            return getHostRightsFromDAO(ipAddress);
        }
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public Integer getHostResNum() {
        return hostResNum;
    }

    public void setHostResNum(Integer hostResNum) {
        this.hostResNum = hostResNum;
    }

    public String getHostRights() {
        return hostRights;
    }

    public void setHostRights(String hostRights) {
        this.hostRights = hostRights;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}
