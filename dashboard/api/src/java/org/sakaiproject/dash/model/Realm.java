/**********************************************************************************
 * $URL$
 * $Id$
 ***********************************************************************************
 *
 * Copyright (c) 2011 The Sakai Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **********************************************************************************/

package org.sakaiproject.dash.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Realm encapsulates information about sakai authz realms, which define who 
 * has access to particular entities in sakai and what permissions they have 
 * with respect to entities in a realm. We need a way to link a dashboard item 
 * to a realm to improve the efficiency of updates.
 *
 */
@Data 
@NoArgsConstructor
@AllArgsConstructor
public class Realm {

	protected Long id;
	protected String realmId;

	public Realm(String realmId) {
		this.realmId = realmId;
	}

}
