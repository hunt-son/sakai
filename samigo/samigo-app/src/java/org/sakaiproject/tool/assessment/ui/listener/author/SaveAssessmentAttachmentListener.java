/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/sam/trunk/samigo-app/src/java/org/sakaiproject/tool/assessment/ui/listener/author/EditPartListener.java $
 * $Id: EditPartListener.java 9268 2006-05-10 21:27:24Z daisyf@stanford.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2004, 2005, 2006 The Sakai Foundation.
 *
 * Licensed under the Educational Community License, Version 1.0 (the"License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.opensource.org/licenses/ecl1.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **********************************************************************************/



package org.sakaiproject.tool.assessment.ui.listener.author;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.tool.assessment.data.ifc.assessment.AssessmentAttachmentIfc;
import org.sakaiproject.tool.assessment.data.ifc.assessment.AssessmentIfc;
import org.sakaiproject.tool.assessment.services.assessment.AssessmentService;
import org.sakaiproject.tool.assessment.ui.bean.author.AssessmentBean;
import org.sakaiproject.tool.assessment.ui.listener.util.ContextUtil;

import org.sakaiproject.content.api.FilePickerHelper;
import org.sakaiproject.tool.cover.SessionManager;
import org.sakaiproject.tool.api.ToolSession;
import org.sakaiproject.entity.api.Reference;

/**
 * <p>Title: Samigo</p>
 * <p>Description: Sakai Assessment Manager</p>
 * <p>Copyright: Copyright (c) 2004 Sakai Project</p>
 * <p>Organization: Sakai Project</p>
 * @version $Id: EditPartListener.java 9268 2006-05-10 21:27:24Z daisyf@stanford.edu $
 */

public class SaveAssessmentAttachmentListener
    implements ActionListener
{
  private static Log log = LogFactory.getLog(SaveAssessmentAttachmentListener.class);
  private static ContextUtil cu;

  public SaveAssessmentAttachmentListener()
  {
  }

  public void processAction(ActionEvent ae) throws AbortProcessingException {

    FacesContext context = FacesContext.getCurrentInstance();
    Map reqMap = context.getExternalContext().getRequestMap();
    Map requestParams = context.getExternalContext().getRequestParameterMap();

    AssessmentBean assessmentBean = (AssessmentBean) cu.lookupBean("assessmentBean");
    String assessmentId = assessmentBean.getAssessmentId();

    // attach item attachemnt to assessmentBean
    ArrayList attachmentList = prepareAssessmentAttachment((AssessmentIfc)assessmentBean.getAssessment().getData());
    assessmentBean.setAttachmentList(attachmentList);
  }

  private ArrayList prepareAssessmentAttachment(AssessmentIfc assessment){
    Set attachmentSet = assessment.getAssessmentAttachmentSet();
    if (attachmentSet == null){
	attachmentSet = new HashSet();
    }
    log.debug("*** attachment size="+attachmentSet.size());
    AssessmentService assessmentService = new AssessmentService();
    String protocol = ContextUtil.getProtocol();
    ToolSession session = SessionManager.getCurrentToolSession();
    if (session.getAttribute(FilePickerHelper.FILE_PICKER_CANCEL) == null &&
        session.getAttribute(FilePickerHelper.FILE_PICKER_ATTACHMENTS) != null) {
      List refs = (List)session.getAttribute(FilePickerHelper.FILE_PICKER_ATTACHMENTS);
      if (refs!=null && refs.size() > 0){
        Reference ref = (Reference)refs.get(0);

        for(int i=0; i<refs.size(); i++) {
          ref = (Reference) refs.get(i);
          log.debug("**** ref.Id="+ref.getId());
          log.debug("**** ref.name="+ref.getProperties().getProperty(									    ref.getProperties().getNamePropDisplayName()));
          AssessmentAttachmentIfc newAttach = assessmentService.createAssessmentAttachment(
                                        assessment,
                                        ref.getId(), ref.getProperties().getProperty(
                                                     ref.getProperties().getNamePropDisplayName()),
                                        protocol);
          attachmentSet.add(newAttach);
        }
      }
    }
    session.removeAttribute(FilePickerHelper.FILE_PICKER_ATTACHMENTS);
    session.removeAttribute(FilePickerHelper.FILE_PICKER_CANCEL);
    ArrayList list = new ArrayList();
    Iterator iter = attachmentSet.iterator();
    while (iter.hasNext()){
      AssessmentAttachmentIfc a = (AssessmentAttachmentIfc)iter.next();
      list.add(a);
    }
    return list;
  }

 }

