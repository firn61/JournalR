package ru.donenergo.journalr.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.context.WebApplicationContext;
import ru.donenergo.journalr.dao.ICommonDAO;
import ru.donenergo.journalr.models.BasicPodstation;
import ru.donenergo.journalr.models.Period;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CommonService implements ICommonService, IDataToModelSetter {

    private ICommonDAO commonDAO;
    private int currentPeriod;
    private List<BasicPodstation> basicPodstationLabels;
    private List<String> podstationTypes;
    private List<Period> periods;
    private String currentActivity;
    private Activity activity;
    private List<String> messages = new ArrayList<>();
    private List<String> errors = new ArrayList<>();
    static final Logger logger = LoggerFactory.getLogger(CommonService.class);

    @Autowired
    public CommonService(ICommonDAO commonDAO) {
        this.commonDAO = commonDAO;
    }

    @PostConstruct
    private void invokeValues() {
        activity = Activity.INDEX;
        currentPeriod = getCurrentPeriodFromDB();
        refreshCommonValues(currentPeriod);
        periods = getPeriodsFromDB(currentPeriod);
    }

    public void refreshCommonValues(int currentPeriod) {
        logger.info("refreshing commonValues for {} period", currentPeriod);
        this.currentPeriod = currentPeriod;
        basicPodstationLabels = getPodstationLabelsFromDB(currentPeriod);
        podstationTypes = getPodstationTypesFromDB(currentPeriod);
    }

    @Override
    public void setDataToModel(Model model) {
        logger.info("values added to model");
        model.addAttribute("messages", new ArrayList<>(messages));
        messages.clear();
        model.addAttribute("errors", new ArrayList<>(errors));
        errors.clear();
        model.addAttribute("podstationTypes", getPodstationTypes());
        model.addAttribute("basicPodstationLabels", getBasicPodstationLabels());
        model.addAttribute("periods", getPeriods());
        model.addAttribute("currentPeriod", getCurrentPeriod());
    }

    public void addMessage(String message) {
        messages.add(message);
    }

    public void addError(String error) {
        if (!error.equals("ok")) {
            errors.add(error);
        }
    }


    public String getViewName(int rn) {
        if (rn == 0) {
            return "";
        } else {
            switch (activity) {
                case SHOW_PODSTATION:
                    return "showpodstation";
                case NOT_FOUND:
                    return "notfound";
                case EDIT_PODSTATION_VALUES:
                    return "editpodstationvalues";
                case EDIT_PODSTATION_PARAMS:
                    return "editpodstationparams";
                case SHOW_HOUSE_SEGMENT:
                    return "showhousesegment";
                case EDIT_HOUSE_SEGMENT:
                    return "edithousesegment";
                default:
                    return "index";
            }
        }
    }

    public void addBasicPodstatinLabel(BasicPodstation newBasicPodstation) {
        basicPodstationLabels.add(newBasicPodstation);
    }

    public void changeViewIfIndex() {
        if (activity.equals(Activity.INDEX)) {
            activity = Activity.SHOW_PODSTATION;
        }
    }

    @Override
    public List<BasicPodstation> getPodstationLabelsFromDB(int dateRn) {
        return commonDAO.getPodstationLabels(dateRn);
    }

    @Override
    public List<String> getPodstationTypesFromDB(int dateRn) {
        return commonDAO.getPodstationTypes(dateRn);
    }

    @Override
    public List<Period> getPeriodsFromDB(int dateRn) {
        return commonDAO.getPeriods(dateRn);
    }

    @Override
    public Integer getCurrentPeriodFromDB() {
        return commonDAO.getCurrentPeriod();
    }

    public int getCurrentPeriod() {
        return currentPeriod;
    }

    public void setCurrentPeriod(int currentPeriod) {
        this.currentPeriod = currentPeriod;
    }

    public List<BasicPodstation> getBasicPodstationLabels() {
        return basicPodstationLabels;
    }

    public void setBasicPodstationLabels(List<BasicPodstation> basicPodstationLabels) {
        this.basicPodstationLabels = basicPodstationLabels;
    }

    public List<String> getPodstationTypes() {
        return podstationTypes;
    }

    public void setPodstationTypes(List<String> podstationTypes) {
        this.podstationTypes = podstationTypes;
    }

    public List<Period> getPeriods() {
        return periods;
    }

    public void setPeriods(List<Period> periods) {
        this.periods = periods;
    }

    public String getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(String currentActivity) {
        this.currentActivity = currentActivity;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
