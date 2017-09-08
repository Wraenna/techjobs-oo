package org.launchcode.controllers;

import org.launchcode.models.*;
import org.launchcode.models.forms.JobForm;
import org.launchcode.models.data.JobData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

/**
 * Created by LaunchCode
 */
@Controller
@RequestMapping(value = "job")
public class JobController {

    private JobData jobData = JobData.getInstance();

    // The detail display for a given Job at URLs like /job?id=17
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, int id) {

        Job requestedJob = jobData.findById(id);

        if (requestedJob == null) {
            return "redirect:";
        } else {
            model.addAttribute("job", requestedJob);
            return "job-detail";
        }
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(Model model) {
        model.addAttribute(new JobForm());
        return "new-job";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(Model model, @Valid JobForm jobForm, Errors errors) {

        if (errors.hasErrors()) {
            model.addAttribute("name", errors);
            model.addAttribute(jobForm.getName());
            return "new-job";
        } else {

            String newJobName = jobForm.getName();

            int employerId = jobForm.getEmployerId();
            int locationId = jobForm.getLocationId();
            int coreCompetencyId = jobForm.getCoreCompetencyId();
            int positionTypeId = jobForm.getPositionTypeId();

            Employer newEmployer = jobData.getEmployers().findById(employerId);
            Location newLocation = jobData.getLocations().findById(locationId);
            CoreCompetency newCoreCompetency = jobData.getCoreCompetencies().findById(coreCompetencyId);
            PositionType newPositionType = jobData.getPositionTypes().findById(positionTypeId);

            Job newJob = new Job(newJobName, newEmployer, newLocation, newPositionType, newCoreCompetency);
            jobData.add(newJob);
            model.addAttribute(newJob);
            return "redirect:/job?id=" + newJob.getId();
        }

    }
}
