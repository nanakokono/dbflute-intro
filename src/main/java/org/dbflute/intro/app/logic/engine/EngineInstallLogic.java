/*
 * Copyright 2014-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.dbflute.intro.app.logic.engine;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.dbflute.intro.app.logic.core.PublicPropertiesLogic;
import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;
import org.dbflute.intro.bizfw.util.ZipUtil;
import org.dbflute.intro.mylasta.direction.IntroConfig;
import org.dbflute.util.DfStringUtil;

/**
 * @author p1us2er0
 * @author jflute
 */
public class EngineInstallLogic {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private IntroConfig introConfig;
    @Resource
    private IntroPhysicalLogic introPhysicalLogic;
    @Resource
    private EnginePhysicalLogic enginePhysicalLogic;
    @Resource
    private PublicPropertiesLogic publicPropertiesLogic;

    // ===================================================================================
    //                                                                            Download
    //                                                                            ========
    public void downloadUnzipping(String dbfluteVersion) {
        if (DfStringUtil.is_Null_or_TrimmedEmpty(dbfluteVersion)) {
            return;
        }
        final String downloadUrl = calcDownloadUrl(dbfluteVersion);
        final File engineDir = introPhysicalLogic.findEngineDir(dbfluteVersion);
        engineDir.getParentFile().mkdirs(); // make 'mydbflute' directory
        final Path zipFile = doDownloadToZip(downloadUrl, engineDir);
        engineDir.mkdirs(); // make 'engine' directory e.g. mydbflute/dbflute-1.1.1
        ZipUtil.decrypt(zipFile.toFile().getPath(), engineDir.getAbsolutePath());
        FileUtils.deleteQuietly(zipFile.toFile());
    }

    private Path doDownloadToZip(String downloadUrl, File engineDir) {
        final Path zipFile = Paths.get(engineDir.getAbsolutePath() + ".zip");
        try (InputStream ins = new URL(downloadUrl).openStream()) {
            Files.copy(ins, zipFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to copy the downloaded data to zip file: " + zipFile, e);
        }
        return zipFile;
    }

    private String calcDownloadUrl(String dbfluteVersion) {
        return publicPropertiesLogic.findProperties().getDBFluteDownloadUrl(dbfluteVersion);
    }

    // ===================================================================================
    //                                                                        Unzip Client
    //                                                                        ============
    public void unzipClient(String dbfluteVersion, File clientDir) {
        ZipUtil.decrypt(enginePhysicalLogic.buildDfClientZipPath(dbfluteVersion), introPhysicalLogic.buildIntroPath());
        introPhysicalLogic.findClientDir("dfclient").renameTo(clientDir);
    }

    // ===================================================================================
    //                                                                       Remove Engine
    //                                                                       =============
    public void remove(String engineVersion) {
        final File engineDir = introPhysicalLogic.findEngineDir(engineVersion);
        if (engineDir.exists()) {
            try {
                FileUtils.deleteDirectory(engineDir);
            } catch (IOException e) {
                throw new IllegalStateException("Failed to delete the engine: " + engineDir, e);
            }
        }
    }
}