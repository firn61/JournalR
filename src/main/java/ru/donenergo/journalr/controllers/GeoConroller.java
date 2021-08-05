package ru.donenergo.journalr.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import ru.donenergo.journalr.models.Geo;
import ru.donenergo.journalr.services.*;

@Controller
public class GeoConroller implements IGeoConroller, IDataWrapper{

    private final CommonService commonService;
    private final PodstationService podstationService;
    private final HostRestrictionService hostRestrictionService;
    private final GeoService geoService;

    public GeoConroller(CommonService commonService, PodstationService podstationService,
                        HostRestrictionService hostRestrictionService, GeoService geoService) {
        this.commonService = commonService;
        this.podstationService = podstationService;
        this.hostRestrictionService = hostRestrictionService;
        this.geoService = geoService;
    }

    @Override
    public void wrapData(Model model) {
        commonService.setDataToModel(model);
        hostRestrictionService.setDataToModel(model);
        podstationService.setDataToModel(model);
    }

    @Override
    public String getMap(Model model, int rn) {
        wrapData(model);
        model.addAttribute("rn", podstationService.getCurrentPodstationRn());
        Geo geo = geoService.getGeo(podstationService.getCurrentPodstation());
        if (geo != null) {
            model.addAttribute("geo", geo);
            return "map";
        } else {
            commonService.addError(IMessageConstants.GEO_NOT_FOUND);
            commonService.setDataToModel(model);
            return "index";
        }
    }
}
