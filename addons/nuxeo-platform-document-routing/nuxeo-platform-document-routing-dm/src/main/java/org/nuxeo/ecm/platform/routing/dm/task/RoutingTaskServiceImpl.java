/*
 * (C) Copyright 2011 Nuxeo SA (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     ldoguin
 */
package org.nuxeo.ecm.platform.routing.dm.task;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.core.api.UnrestrictedSessionRunner;
import org.nuxeo.ecm.platform.routing.dm.api.RoutingTaskConstants;
import org.nuxeo.ecm.platform.task.Task;
import org.nuxeo.ecm.platform.task.core.service.TaskServiceImpl;

/**
 *
 */
public class RoutingTaskServiceImpl extends TaskServiceImpl implements
        RoutingTaskService {

    @Override
    public List<Task> createRoutingTask(CoreSession coreSession,
            NuxeoPrincipal principal, DocumentModel document, String taskName,
            List<String> prefixedActorIds, boolean createOneTaskPerActor,
            String directive, String comment, Date dueDate,
            Map<String, String> taskVariables, String parentPath)
            throws ClientException {
        final List<Task> tasks = super.createTask(coreSession, principal,
                document, taskName, prefixedActorIds, createOneTaskPerActor,
                directive, comment, dueDate, taskVariables, parentPath);
        new UnrestrictedSessionRunner(coreSession) {

            @Override
            public void run() throws ClientException {
                for (Task task : tasks) {
                    DocumentModel taskDoc = task.getDocument();
                    taskDoc.addFacet(RoutingTaskConstants.ROUTING_TASK_FACET_NAME);
                    session.saveDocument(taskDoc);
                }
            }
        }.runUnrestricted();
        return tasks;
    }

}