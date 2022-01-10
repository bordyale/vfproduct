/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/

package org.apache.ofbiz.boragency;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilDateTime;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilNumber;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ModelService;
import org.apache.ofbiz.service.ServiceUtil;

/**
 * Services for Agreement (Accounting)
 */

public class VfProductServices {

	public static final String module = VfProductServices.class.getName();

	public static final String resource = "VfProductUiLabels";

	public static Map<String, Object> createVfProductJava(DispatchContext ctx, Map<String, Object> context) {
		Delegator delegator = ctx.getDelegator();
		LocalDispatcher dispatcher = ctx.getDispatcher();
		Locale locale = (Locale) context.get("locale");
		String errMsg = null;
		Map<String, Object> result = ServiceUtil.returnSuccess();

		GenericValue userLogin = (GenericValue) context.get("userLogin");

		try {

			String productId = (String) context.get("productId");

			Map<String, Object> tmpResult = dispatcher.runSync("createVfProduct", context);
			result.put("productId", tmpResult.get("productId"));

			if (productId == null) {
				context.put("productId", tmpResult.get("productId"));
			}
			dispatcher.runSync("createVfProductGroup", context);

		} catch (GenericServiceException e) {
			Debug.logWarning(e, module);
			Map<String, String> messageMap = UtilMisc.toMap("errMessage", e.getMessage());
			errMsg = UtilProperties.getMessage(resource, "RefRevenueZero", messageMap, locale);
			return ServiceUtil.returnError(errMsg);
		}
		return result;
	}

	public static Map<String, Object> updateVfProductJava(DispatchContext ctx, Map<String, Object> context) {
		Delegator delegator = ctx.getDelegator();
		LocalDispatcher dispatcher = ctx.getDispatcher();
		Locale locale = (Locale) context.get("locale");
		String errMsg = null;
		Map<String, Object> result = ServiceUtil.returnSuccess();

		GenericValue userLogin = (GenericValue) context.get("userLogin");

		try {

			String productId = (String) context.get("productId");

			
			
			Map<String, Object> tmpResult = dispatcher.runSync("updateVfProduct", context);
			

			GenericValue productGroup = EntityQuery.use(delegator).from("VfProductGroup").where("productId", productId).cache().queryFirst();

			if (productGroup != null) {
				context.put("prodGroupId", productGroup.get("prodGroupId"));
				dispatcher.runSync("updateVfProductGroup", context);
			}else{
				dispatcher.runSync("createVfProductGroup", context);
			}

		} catch (GenericServiceException e) {
			Debug.logWarning(e, module);
			Map<String, String> messageMap = UtilMisc.toMap("errMessage", e.getMessage());
			errMsg = UtilProperties.getMessage(resource, "RefRevenueZero", messageMap, locale);
			return ServiceUtil.returnError(errMsg);
		} catch (GenericEntityException e) {
			Debug.logWarning(e, module);
			Map<String, String> messageMap = UtilMisc.toMap("errMessage", e.getMessage());
			errMsg = UtilProperties.getMessage(resource, "RefRevenueZero", messageMap, locale);
			return ServiceUtil.returnError(errMsg);
		}
		return result;
	}
}
