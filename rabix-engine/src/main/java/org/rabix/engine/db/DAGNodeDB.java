package org.rabix.engine.db;

import java.util.UUID;

import org.rabix.bindings.model.dag.DAGNode;
import org.rabix.common.helper.InternalSchemaHelper;
import org.rabix.engine.store.lru.dag.DAGCache;
import org.rabix.engine.store.repository.DAGRepository;

import com.google.inject.Inject;

/**
 * In-memory {@link DAGNode} repository
 */
public class DAGNodeDB {

  private DAGRepository dagRepository;
  private DAGCache dagCache;

  /**
   *
   * @param dagRepository
   * @param dagCache
   */
  @Inject
  public DAGNodeDB(DAGRepository dagRepository, DAGCache dagCache) {
    this.dagRepository = dagRepository;
    this.dagCache = dagCache;
  }

  /**
   * Tries to get a node from cache, than fallbacks to repository and updates cache
   * @param id
   * @param rootId
   * @param dagHash
   * @return
   */
  public DAGNode get(String id, UUID rootId, String dagHash) {
    DAGNode res = dagCache.get(id, rootId, dagHash);
    if(res == null) {
      res = dagRepository.get(id, rootId);
      dagCache.put(dagRepository.get(InternalSchemaHelper.ROOT_NAME, rootId), rootId);
    }
    return res;
  }

  /**
   * Puts node into repository and cache
   * @param node
   * @param rootId
   * @return String hash of inserted node
   */
  public String put(DAGNode node, UUID rootId) {
    String dagHash = dagCache.put(node, rootId);
    dagRepository.insert(rootId, node);
    return dagHash;
  }
  
}
