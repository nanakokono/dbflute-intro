<create>
  <h2 class="heading">Create Client</h2>
  <div class="ui form">
    <div class="ui stackable two column divided grid">
      <div class="row">
        <div class="column">
          <div class="required field">
            <label>Project Name
              <!--{{'LABEL_projectName' | translate:translationData}}-->
            </label>
            <input type="text" ref="projectName" placeholder="maihamadb" />
          </div>
        </div>
      </div>
      <div class="row">
        <div class="column">
          <div class="required field">
            <label>DBFlute Version
              <!--{{'LABEL_dbfluteVersion' | translate:translationData}}-->
            </label>
            <su-dropdown ref="dbfluteVersion" items="{ versions }"></su-dropdown>
          </div>
          <div class="required field">
            <label>DBMS
              <!--{{'LABEL_databaseCode' | translate:translationData}}-->
            </label>
            <su-dropdown ref="databaseCode" items="{ targetDatabaseItems }"></su-dropdown>
          </div>
          <div class="required field">
            <label>JDBC Driver
              <!--{{'LABEL_jdbcDriverFqcn' | translate:translationData}}-->
            </label>
            <input type="text" ref="jdbcDriverFqcn" placeholder="com.mysql.jdbc.Driver" value="com.mysql.jdbc.Driver" />
          </div>
          <div class="required field" if="{ needsJdbcDriver }">
            <label>JDBC Driver Jar
              <!--{{'LABEL_jdbcDriverFile' | translate:translationData}}-->
            </label>
            <input type="file" file-input delegate-file-drop max-bytes="5242880" ng-model="update.file" ng-file-change="changeFile">
          </div>
        </div>
        <div class="column">
          <!-- <button onclick="{ openORMapperOptions }">O/R Mapper Options</button> -->
          <div class="required field" if="{ oRMapperOptionsFlg }">
            <label>Language
              <!--{{'LABEL_languageCode' | translate:translationData}}-->
            </label>
            <su-dropdown ref="languageCode" items="{ targetLanguageItems }"></su-dropdown>
          </div>
          <div class="required field" if="{ oRMapperOptionsFlg }">
            <label>DI Container
              <!--{{'LABEL_containerCode' | translate:translationData}}-->
            </label>
            <su-dropdown ref="containerCode" items="{ targetContainerItems }"></su-dropdown>
          </div>
          <div class="required field" if="{ oRMapperOptionsFlg }">
            <label>Generation Package
              <!--{{'LABEL_packageBase' | translate:translationData}}-->
            </label>
            <input type="text" ref="packageBase" value="org.docksidestage.dbflute" />
          </div>
        </div>
      </div>
      <div class="row">
        <div class="column">
          <div class="required field">
            <label>URL
              <!--{{'LABEL_url' | translate:translationData}}-->
            </label>
            <input type="text" ref="url" placeholder="jdbc:mysql://localhost:3306/maihamadb" />
          </div>
          <div class="field">
            <label>Schema
              <!--{{'LABEL_schema' | translate:translationData}}-->
            </label>
            <input type="text" ref="schema" placeholder="MAIHAMADB" />
          </div>
          <div class="required field">
            <label>User
              <!--{{'LABEL_user' | translate:translationData}}-->
            </label>
            <input type="text" ref="user" placeholder="maihamadb" />
          </div>
          <div class="field">
            <label>Password
              <!--{{'LABEL_password' | translate:translationData}}-->
            </label>
            <input type="text" ref="password" />
          </div>
          <div class="field">
            <su-checkbox ref="testConnection">Connect as test</su-checkbox>
          </div>
          <div class="field">
            <button class="ui button primary" onclick="{ create }">Create</button>
          </div>
        </div>
      </div>
    </div>

  </div>

  <script>
    import _ApiFactory from '../common/factory/ApiFactory.js'
    const ApiFactory = new _ApiFactory()

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    this.classificationMap = {} // e.g. targetDatabase
    this.needsJdbcDriver = false
    this.oRMapperOptionsFlg = true
    this.versions = []
    const self = this

    // ===================================================================================
    //                                                                      Initial Method
    //                                                                      ==============
    this.findClassifications = function () {
      ApiFactory.classifications().then(function (json) {
        self.targetDatabaseItems = Object.keys(json.targetDatabaseMap).map(key => {
          return { value: key, label: json.targetDatabaseMap[key].databaseName }
        })
        self.targetLanguageItems = Object.keys(json.targetLanguageMap).map(key => {
          return { value: key, label: json.targetLanguageMap[key] }
        })
        self.targetContainerItems = Object.keys(json.targetContainerMap).map(key => {
          return { value: key, label: json.targetContainerMap[key] }
        })
        self.classificationMap = json
        self.update()
      })
    }
    this.engineVersions = function () {
      ApiFactory.engineVersions().then(function (json) {
        self.versions = json.map(element => {
          return { label: element, value: element }
        })
        self.update()
      })
    }
    this.setLatestEngineVerison = function () {
      ApiFactory.engineLatest().then(function (json) {
        self.refs.dbfluteVersion.value = json.latestReleaseVersion
        self.update()
      })
    }

    // ===================================================================================
    //                                                                        Event Method
    //                                                                        ============
    this.openORMapperOptions = function () {
      this.oRMapperOptionsFlg = !this.oRMapperOptionsFlg
    }
    const changeDatabase = function (databaseCode) {
      let database = self.classificationMap['targetDatabaseMap'][databaseCode]
      console.log(database)
      // switch showing JDBCDriver select form
      self.needsJdbcDriver = !database.embeddedJar
      // initialize JDBC Driver
      // self.refs.jdbcDriver.value = null
      self.refs.jdbcDriverFqcn.value = database.driverName
      self.refs.url.value = database.urlTemplate
      self.refs.schema.value = database.defaultSchema
    }
    this.create = function () {
      const client = {
        create: true,
        mainSchemaSettings: {},
        schemaSyncCheckMap: {},
        dbfluteVersion: self.refs.dbfluteVersion.value,
        jdbcDriver: null
      }
      const testConnection = self.refs.testConnection.checked
      ApiFactory.createClient(client, testConnection).then(function (response) {
        route('main')
      })
    }
    this.changeFile = function (files) {
      let file = files[0]
      let reader = new FileReader()
      reader.onload = (function () {
        return function () {
          // encode base64
          let result = window.btoa(reader.result)
          this.client.jdbcDriver = { fileName: null, data: null }
          this.client.jdbcDriver.fileName = file.name
          this.client.jdbcDriver.data = result
        }
      }(file))
      if (file) {
        reader.readAsBinaryString(file)
      }
    }

    // ===================================================================================
    //                                                                          Initialize
    //                                                                          ==========
    this.on('mount', () => {
      this.findClassifications()
      this.engineVersions()
      this.setLatestEngineVerison()

      this.refs.databaseCode.on('change', target => {
        changeDatabase(target.value)
        this.update()
      })
    })
  </script>
</create>