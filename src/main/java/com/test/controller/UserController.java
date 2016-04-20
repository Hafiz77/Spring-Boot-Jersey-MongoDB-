package com.test.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.test.entity.User;
import com.test.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Hafiz on 4/19/2016.
 */


@Path("/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Autowired
    private UserService userService;

    Gson gson = new Gson();

    /******************************* Create **********************************/
    /**
     * @param user json object
     */

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response create(User user) {

        Map<Object, Object> apiResponse = new HashMap<Object, Object>();
        Map<Object, Object> serviceResponse = new HashMap<Object, Object>();

        try {
            Set<ConstraintViolation<User>> validateErrors = validator.validate(user);
            //logger.info("creating new user email=" + user.getEmail());


            if (validateErrors.isEmpty()) {

                logger.debug("Calling user service: " + user.getEmail());

                user = (User) userService.create(user);

                logger.debug("Done calling userservice");

                if (user != null) {

                    logger.debug("User done creating user with email :" + user.getEmail());

                    //serviceResponse.put("created", user);
                    apiResponse.put("apiresponse", user);

                    return Response.ok(apiResponse).build();
                }

            } else {
                for (ConstraintViolation<User> error : validateErrors) {
                    apiResponse.put(error.getPropertyPath().toString(),
                            error.getMessage());
                }
                return Response.status(400).entity(apiResponse).build();
            }
        }
        /*catch (DuplicateKeyException e){

            logger.error("Error occured creating user:",e);
            apiResponse.put("error", "Duplicate user found for emaill address:"+user.getEmail());
        }*/ catch (Exception e) {

            logger.error("Error occured creating user:", e);
            apiResponse.put("error", e.getMessage());
        }
        return Response.status(500).entity(apiResponse).build();

    }

    /******************************* Retrieve **********************************/
    /**
     * Get register user
     *
     * @param uId
     *
     * @return user Object + 200 on success / 500 + error message on failure
     *
     *
     * @summary Get register user
     */

    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Response get(@PathParam("id") String uId) {

        Map<Object, Object> apiResponse = new HashMap<Object, Object>();
        Map<Object, Object> serviceResponse = new HashMap<Object, Object>();

        try {

            logger.info("Retreiving  user with id=" + uId);

            User u = userService.get(uId);

            logger.info("User service called with this id=" + uId);

            if (u != null) {

                logger.info("User retrieved with =" + uId);
                //serviceResponse.put("retreived", u);
                apiResponse.put("apiresponse", u);

                return Response.ok(apiResponse).build();

            } else {

                logger.info("User not retrived =" + uId);
                serviceResponse.put("success",Boolean.FALSE);
                serviceResponse.put("message","User not found");
                apiResponse.put("response", serviceResponse);

            }

        } catch (Exception e) {
            logger.error("Error retriving user", e);
            apiResponse.put("error", e.getMessage());
        }

        return Response.status(500).entity(apiResponse).build();
    }


    /******************************* Update **********************************/
    /**
     * Update user
     *
     * @param user - object
     *
     * @return user id & session token + 200 on success / 400 + error message on failure
     *
     *
     * @summary Update user
     */

    @PUT
    @Path("/{id}")
    @Consumes("application/json")
    @Produces("application/json")
    public Response update(User user) {

        Map<Object, Object> apiResponse = new HashMap<Object, Object>();
        Map<Object, Object> serviceResponse = new HashMap<Object, Object>();
        String objToJson = gson.toJson(user);
        JsonParser parser = new JsonParser();

        JsonObject userJO = parser.parse(objToJson).getAsJsonObject();

        // logger.info("Start Updating user  :" + objToJson);

        try {

            User findUser = userService.get(user.getId());
            if (findUser == null) {
                logger.info("user_not_found");
                apiResponse.put("user_not_found","user.primary.email.not.found");
                return Response.status(404).entity(apiResponse).build();
            }


            logger.info("Validating update user with=" + user.getEmail());
            Set<ConstraintViolation<User>> validateErrors = validator
                    .validate(user);


            if (validateErrors.isEmpty()) {

                logger.info("Calling userservice to update user with=" + user.getEmail());
                user = userService.update(user);

                if (user != null) {
                    objToJson = gson.toJson(user);
                    logger.info("update user info " + objToJson);

                    logger.info("Updated user=" + user.getEmail());


                    //serviceResponse.put("update", user);
                    //apiResponse.put("apiresponse", serviceResponse);
                    apiResponse.put("updatedUser", user);

                    return Response.ok(apiResponse).build();

                }


            } else {

                logger.info("Validation error found =" + user.getEmail());

                for (ConstraintViolation<User> error : validateErrors) {
                    apiResponse.put(error.getPropertyPath().toString(),
                            error.getMessage());
                }
                return Response.status(400).entity(apiResponse).build();
            }


        } catch (Exception e) {

            logger.error("", e);
            apiResponse.put("error", e.getMessage());
            logger.info(e.getMessage());

        }
        return Response.status(500).entity(apiResponse).build();
    }


    /******************************* Delete **********************************/
    /**
     * Delete user
     *
     * @param uId - user id
     *
     * @return user id & session token + 200 on success / 400 + error message on failure
     *
     *
     * @summary Update user
     */

    @DELETE
    @Path("/{uId}")
    @Produces("application/json")
    public Response deleteUser(@PathParam("uId") String uId) {

        Map<Object, Object> apiResponse = new HashMap<Object, Object>();
        Map<Object, Object> serviceResponse  = new HashMap<Object, Object>();

        try {

            logger.info("Deleting user=" + uId);

            boolean b = userService.delete(uId);
            //serviceResponse.put("deleted", b);
            apiResponse.put("apiresponse", b);

            return Response.ok(apiResponse).build();
        } catch (Exception e) {

            logger.error("Error", e);
            apiResponse.put("error", e.getMessage());
        }

        return Response.status(500).entity(apiResponse).build();
    }

    /******************************* list **********************************/
    /**
     * @param offset
     * @param limit
     * @return JSON list
     * @summary Get user list
     */

    @GET
    @Path("/list/offset/{offset}/limit/{limit}")
    @Produces("application/json")
    public Response list(@PathParam("offset") Integer offset,
                         @PathParam("limit") Integer limit) {

        Map<Object, Object> apiResponse     = new HashMap<Object, Object>();
        Map<Object, Object> serviceResponse = new HashMap<Object, Object>();

        try {

            logger.debug("Getting users list by limit=" + limit + " offset="+ offset);

            List<User> users = userService.list(offset, limit);

            serviceResponse.put("offset", offset);
            serviceResponse.put("limit", limit);
            serviceResponse.put("total", users.size());
            serviceResponse.put("list", users);

            apiResponse.put("apiresponse", serviceResponse);

            return Response.ok(apiResponse).build();

        } catch (Exception e) {

            logger.error("Error in getting user list:", e);
            apiResponse.put("error", e.getMessage());
        }

        return Response.status(500).entity(apiResponse).build();
    }






}
