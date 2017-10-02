package com.csipon.crm.controller.message;

/**
 * Created by bpogo on 5/6/2017.
 */
public final class MessageProperty {
    //Validation Keys
    public static final String ERROR_CODE_REQUIRED = "validation.required";
    public static final String ERROR_CODE_USER_ALREADY_EXIST = "validation.user-already-exist";
    public static final String ERROR_CODE_PRODUCT_PARAM_ALREADY_EXIST = "validation.productParam-already-exist";    
    public static final String ERROR_CODE_WRONG_FORMAT = "validation.wrong-format";
    public static final String ERROR_CODE_DIFFERENT_STATUS = "validation.bulk.different-status";
    public static final String REPLACE_WILD_CARD = "%wild_card%";

    //Success Keys
    public static final String SUCCESS_USER_CREATED = "success.user-create";
    public static final String SUCCESS_USER_UPDATED = "success.user-update";
    public static final String SUCCESS_PASSWORD_UPDATED = "success.password-update";
    public static final String SUCCESS_PRODUCT_CREATED = "success.product-create";
    public static final String SUCCESS_PRODUCT_UPDATE = "success.product-update";
    public static final String SUCCESS_PRODUCT_PARAM_CREATED = "success.productParam-create";
    public static final String SUCCESS_PRODUCT_PARAM_UPDATED = "success.productParam-update";
    public static final String SUCCESS_PRODUCT_PARAM_DELETED = "success.productParam-delete";    
    public static final String SUCCESS_DISCOUNT_CREATED = "success.discount-create";
    public static final String SUCCESS_DISCOUNT_UPDATED = "success.discount-update";
    public static final String SUCCESS_GROUP_CREATED = "success.group-create";
    public static final String SUCCESS_GROUP_UPDATE = "success.group-update";
    public static final String SUCCESS_COMPLAINT_CREATED = "success.complaint-create";
    public static final String SUCCESS_ORDER_CREATED = "success.order-create";
    public static final String SUCCESS_PRODUCT_BULK_UPDATED = "success.product-bulk-updated";
    public static final String SUCCESS_REGION_UPDATE = "success.region-update";

    public static final String SUCCESS_DISCOUNT_BULK_UPDATED = "success.discount-bulk-updated";
    public static final String SUCCESS_GROUP_BULK_UPDATED = "success.group-bulk-updated";

    //Error Keys
    public static final String ERROR_SERVER_ERROR = "error.server-error";
    public static final String ERROR_LIFECYCLE_ORDER = "error.order.transition-not-allowed";

    public static final String SUCCESS_ORDER_ACCEPT = "success.order-accept";
    public static final String SUCCESS_ORDER_ACTIVATE = "success.order-activate";
    public static final String SUCCESS_ORDER_RESUME = "success.order-resume";
    public static final String SUCCESS_ORDER_PAUSE = "success.order-pause";
    public static final String SUCCESS_ORDER_DISABLE = "success.order-disable";
    public static final String SUCCESS_ORDER_REQUEST_DISABLE = "success.order-request-disable";
    public static final String SUCCESS_ORDER_REQUEST_PAUSE = "success.order-request-pause";
    public static final String SUCCESS_ORDER_REQUEST_RESUME = "success.order-request-resume";


    public static final String SUCCESS_ORDER_NOT_ACCEPT = "success.order-not-accept";
    public static final String SUCCESS_ORDER_NOT_ACTIVATE = "success.order-not-activate";
    public static final String SUCCESS_ORDER_NOT_RESUME = "success.order-not-resume";
    public static final String SUCCESS_ORDER_NOT_PAUSE = "success.order-not-pause";
    public static final String SUCCESS_ORDER_NOT_DISABLE = "success.order-not-disable";
    public static final String SUCCESS_ORDER_ERROR_REQUEST_DISABLE = "success.order-error-request-disable";
    public static final String SUCCESS_ORDER_ERROR_REQUEST_PAUSE = "success.order-error-request-pause";
    public static final String SUCCESS_ORDER_ERROR_REQUEST_RESUME = "success.order-error-request-resume";


    private MessageProperty() {
    }
}
