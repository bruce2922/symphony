/*
 * Copyright (c) 2012, B3log Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.b3log.symphony.service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.b3log.latke.Keys;
import org.b3log.latke.repository.Query;
import org.b3log.latke.repository.RepositoryException;
import org.b3log.latke.repository.SortDirection;
import org.b3log.latke.service.ServiceException;
import org.b3log.latke.util.CollectionUtils;
import org.b3log.symphony.model.Comment;
import org.b3log.symphony.repository.CommentRepository;
import org.json.JSONObject;

/**
 * Comment management service.
 * 
 * @author <a href="mailto:DL88250@gmail.com">Liang Ding</a>
 * @version 1.0.0.0, Oct 7, 2012
 * @since 0.2.0
 */
public final class CommentQueryService {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(CommentQueryService.class.getName());
    /**
     * Singleton.
     */
    private static final CommentQueryService SINGLETON = new CommentQueryService();
    /**
     * Comment repository.
     */
    private CommentRepository commentRepository = CommentRepository.getInstance();

    /**
     * Gets the article comments with the specified article id, page number and page size.
     * 
     * @param articleId the specified article id
     * @param currentPageNum the specified page number
     * @param pageSize the specified page size
     * @return comments, return an empty list if not found
     * @throws ServiceException service exception
     */
    public List<JSONObject> getArticleComments(final String articleId, final int currentPageNum, final int pageSize)
            throws ServiceException {
        final Query query = new Query().addSort(Comment.COMMENT_CREATE_TIME, SortDirection.DESCENDING)
                .setPageCount(currentPageNum).setPageSize(pageSize);
        try {
            final JSONObject result = commentRepository.get(query);
            final List<JSONObject> ret = CollectionUtils.<JSONObject>jsonArrayToList(result.optJSONArray(Keys.RESULTS));

            return ret;
        } catch (final RepositoryException e) {
            LOGGER.log(Level.SEVERE, "Gets article [" + articleId + "] comments failed", e);
            throw new ServiceException(e);
        }
    }

    /**
     * Gets the {@link CommentQueryService} singleton.
     *
     * @return the singleton
     */
    public static CommentQueryService getInstance() {
        return SINGLETON;
    }

    /**
     * Private constructor.
     */
    private CommentQueryService() {
    }
}