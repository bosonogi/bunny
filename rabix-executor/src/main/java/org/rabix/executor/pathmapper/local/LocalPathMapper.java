package org.rabix.executor.pathmapper.local;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.rabix.bindings.filemapper.FileMapper;
import org.rabix.bindings.filemapper.FileMappingException;
import org.rabix.executor.config.StorageConfiguration;

import com.google.inject.Inject;

public class LocalPathMapper implements FileMapper {

  private final StorageConfiguration storageConfig;
  
  @Inject
  public LocalPathMapper(final StorageConfiguration storageConfig) {
    this.storageConfig = storageConfig;
  }
  
  @Override
  public String map(String path, Map<String, Object> config) throws FileMappingException {
    if (!path.startsWith(File.separator)) {
      try {
        return new File(storageConfig.getLocalExecutionDirectory(), path).getCanonicalPath();
      } catch (IOException e) {
        throw new FileMappingException(e);
      }
    }
    return path;
  }

}
