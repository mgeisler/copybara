/*
 * Copyright (C) 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.copybara;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.copybara.authoring.Authoring;
import com.google.copybara.folder.FolderDestinationOptions;
import com.google.copybara.folder.FolderModule;
import com.google.copybara.folder.FolderOriginOptions;
import com.google.copybara.git.GerritOptions;
import com.google.copybara.git.GitDestinationOptions;
import com.google.copybara.git.GitMirrorOptions;
import com.google.copybara.git.GitModule;
import com.google.copybara.git.GitOptions;
import com.google.copybara.git.GitOriginOptions;
import com.google.copybara.git.GithubDestinationOptions;
import com.google.copybara.git.GithubOptions;
import com.google.copybara.git.GithubPrOriginOptions;
import com.google.copybara.modules.PatchModule;
import com.google.copybara.transform.metadata.MetadataModule;
import com.google.copybara.util.console.Console;
import java.nio.file.FileSystem;
import java.util.Map;

/**
 * A supplier of modules and {@link Option}s for Copybara.
 */
public class ModuleSupplier {

  private static final ImmutableSet<Class<?>> BASIC_MODULES = ImmutableSet.of(
      GlobModule.class,
      Core.class,
      Authoring.Module.class,
      FolderModule.class,
      GitModule.class,
      MetadataModule.class,
      PatchModule.class);
  private final Map<String, String> environment;
  private final FileSystem fileSystem;
  private final Console console;

  public ModuleSupplier(Map<String, String> environment, FileSystem fileSystem,
      Console console) {
    this.environment = Preconditions.checkNotNull(environment);
    this.fileSystem = Preconditions.checkNotNull(fileSystem);
    this.console = Preconditions.checkNotNull(console);
  }

  /**
   * Returns the {@code set} of modules available.
   */
  public ImmutableSet<Class<?>> getModules() {
    return BASIC_MODULES;
  }

  /** Returns a new list of {@link Option}s. */
  public Options newOptions() {
    GeneralOptions generalOptions = new GeneralOptions(environment, fileSystem, console);
    GitOptions gitOptions = new GitOptions(generalOptions);
    GitDestinationOptions gitDestinationOptions =
        new GitDestinationOptions(generalOptions, gitOptions);
    return new Options(ImmutableList.of(
        generalOptions,
        new FolderDestinationOptions(),
        new FolderOriginOptions(),
        gitOptions,
        new GitOriginOptions(),
        new GithubPrOriginOptions(),
        gitDestinationOptions,
        new GithubOptions(generalOptions, gitOptions),
        new GithubDestinationOptions(),
        new GerritOptions(generalOptions, gitOptions),
        new GitMirrorOptions(generalOptions, gitOptions),
        new PatchingOptions(generalOptions),
        new WorkflowOptions()));
  }
}