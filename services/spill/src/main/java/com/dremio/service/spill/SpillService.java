/*
 * Copyright (C) 2017-2019 Dremio Corporation
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
package com.dremio.service.spill;

import com.dremio.common.exceptions.UserException;
import com.dremio.service.Service;

/**
 * Tracks spill files generated by operators that support spilling (such as sort, hash agg, etc.)
 */
public interface SpillService extends Service {

  // Deprecated functions, used for supporting SpillManager-based code, while it moves to the *SpillStream interfaces

  /**
   * Make a sub-directory 'id' in each of the healthy spill directories. Caller needs to later delete it by calling
   * {@link #deleteSpillSubdirs(String)}
   * @param id Name of the directory being created. Should be based on the quadruplet of IDs: query,major,minor,operator
   * @throws UserException Will throw a UserException.dataWriteError(), if unable to create the sub-directory 'id' in
   *                       all spill directories
   */
  @Deprecated
  void makeSpillSubdirs(String id) throws UserException;

  /**
   * Delete the sub-directory 'id' in each of the spill directories
   * @param id Name of the directory being deleted. Should match the ID passed into {@link #makeSpillSubdirs(String)} .
   */
  @Deprecated
  void deleteSpillSubdirs(String id);

  /**
   * Returns a random (healthy) spill sub-directory
   * @param id Parameter that matches the id used in {@link #makeSpillSubdirs(String)}
   * @throws UserException Will throw a UserException.dataWriteError() if unable to find a single healthy sub-directory
   */
  @Deprecated
  SpillDirectory getSpillSubdir(String id) throws UserException;
}