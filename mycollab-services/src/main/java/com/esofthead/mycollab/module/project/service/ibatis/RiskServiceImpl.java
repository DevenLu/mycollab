/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.project.service.ibatis;

import com.esofthead.mycollab.cache.CleanCacheEvent;
import com.esofthead.mycollab.common.ModuleNameConstants;
import com.esofthead.mycollab.common.interceptor.aspect.ClassInfo;
import com.esofthead.mycollab.common.interceptor.aspect.ClassInfoMap;
import com.esofthead.mycollab.common.interceptor.aspect.Traceable;
import com.esofthead.mycollab.common.interceptor.aspect.Watchable;
import com.esofthead.mycollab.core.persistence.ICrudGenericDAO;
import com.esofthead.mycollab.core.persistence.ISearchableDAO;
import com.esofthead.mycollab.core.persistence.service.DefaultService;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.dao.RiskMapper;
import com.esofthead.mycollab.module.project.dao.RiskMapperExt;
import com.esofthead.mycollab.module.project.domain.Risk;
import com.esofthead.mycollab.module.project.domain.SimpleRisk;
import com.esofthead.mycollab.module.project.domain.criteria.RiskSearchCriteria;
import com.esofthead.mycollab.module.project.esb.DeleteProjectRiskEvent;
import com.esofthead.mycollab.module.project.service.*;
import com.google.common.eventbus.AsyncEventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Service
@Transactional
@Traceable(nameField = "riskname", extraFieldName = "projectid")
@Watchable(userFieldName = "assigntouser", extraTypeId = "projectid")
public class RiskServiceImpl extends DefaultService<Integer, Risk, RiskSearchCriteria> implements RiskService {

    static {
        ClassInfoMap.put(RiskServiceImpl.class, new ClassInfo(ModuleNameConstants.PRJ, ProjectTypeConstants.RISK));
    }

    @Autowired
    private RiskMapper riskMapper;

    @Autowired
    private RiskMapperExt riskMapperExt;

    @Autowired
    private AsyncEventBus asyncEventBus;

    @Override
    public ICrudGenericDAO<Integer, Risk> getCrudMapper() {
        return riskMapper;
    }

    @Override
    public ISearchableDAO<RiskSearchCriteria> getSearchMapper() {
        return riskMapperExt;
    }

    @Override
    public SimpleRisk findById(Integer riskId, Integer sAccountId) {
        return riskMapperExt.findRiskById(riskId);
    }

    @Override
    public Integer saveWithSession(Risk record, String username) {
        Integer recordId = super.saveWithSession(record, username);
        asyncEventBus.post(new CleanCacheEvent(record.getSaccountid(), new Class[]{ProjectService.class,
                ProjectGenericTaskService.class, ProjectActivityStreamService.class}));
        return recordId;
    }

    @Override
    public Integer updateWithSession(Risk record, String username) {
        int result = super.updateWithSession(record, username);
        asyncEventBus.post(new CleanCacheEvent(record.getSaccountid(), new Class[]{ProjectService.class,
                ProjectGenericTaskService.class, ProjectActivityStreamService.class}));
        return result;
    }

    @Override
    public void removeByCriteria(RiskSearchCriteria criteria, Integer accountId) {
        super.removeByCriteria(criteria, accountId);
        asyncEventBus.post(new CleanCacheEvent(accountId, new Class[]{ProjectService.class,
                ProjectGenericTaskService.class, ProjectActivityStreamService.class, ItemTimeLoggingService.class}));
    }

    @Override
    public void massRemoveWithSession(List<Risk> items, String username, Integer accountId) {
        super.massRemoveWithSession(items, username, accountId);
        asyncEventBus.post(new CleanCacheEvent(accountId, new Class[]{ProjectService.class,
                ProjectGenericTaskService.class, ProjectActivityStreamService.class, ItemTimeLoggingService.class}));
        DeleteProjectRiskEvent event = new DeleteProjectRiskEvent(items.toArray(new Risk[items.size()]),
                username, accountId);
        asyncEventBus.post(event);
    }
}
