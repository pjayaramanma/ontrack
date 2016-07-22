package net.nemerosa.ontrack.dsl

import net.nemerosa.ontrack.dsl.doc.DSL
import net.nemerosa.ontrack.dsl.doc.DSLMethod

@DSL(value = "General configuration of Ontrack.")
class Config {

    private final Ontrack ontrack

    Config(Ontrack ontrack) {
        this.ontrack = ontrack
    }

    @DSLMethod("Checks if the projects are accessible in anonymous mode.")
    boolean getGrantProjectViewToAll() {
        def settings = ontrack.get('settings/general-security')
        return settings.grantProjectViewToAll
    }

    /**
     * Updates security settings
     */
    @DSLMethod("Sets if the projects are accessible in anonymous mode.")
    def setGrantProjectViewToAll(boolean grantProjectViewToAll) {
        ontrack.put(
                'settings/general-security',
                [
                        grantProjectViewToAll: grantProjectViewToAll
                ]
        )
    }

    /**
     * Creates or update a GitHub configuration.
     */

    def gitHub(String name) {
        gitHub([:], name)
    }

    def gitHub(Map<String, ?> parameters, String name) {
        def params = parameters + [name: name]
        ontrack.post(
                'extension/github/configurations/create',
                params
        )
    }

    /**
     * Gets the list of all GitHub configuration names
     */
    List<String> getGitHub() {
        ontrack.get('extension/github/configurations/descriptors').resources.collect { it.id }
    }

    /**
     * Stash configurations.
     */

    def stash(Map<String, ?> parameters, String name) {
        def params = parameters + [name: name]
        ontrack.post(
                'extension/stash/configurations/create',
                params
        )
    }

    List<String> getStash() {
        ontrack.get('extension/stash/configurations/descriptors').resources.collect { it.id }
    }

    /**
     * Creates or update a Git configuration
     */
    def git(Map<String, ?> parameters, String name) {
        def params = parameters + [name: name]
        ontrack.post(
                'extension/git/configurations/create',
                params
        )
    }

    /**
     * Gets the list of all Git configuration names
     */
    List<String> getGit() {
        ontrack.get('extension/git/configurations/descriptors').resources.collect { it.id }
    }

    def svn(Map<String, ?> parameters, String name) {
        def params = parameters + [name: name]
        ontrack.post(
                'extension/svn/configurations/create',
                params
        )
    }

    def getSvn() {
        ontrack.get('extension/svn/configurations/descriptors').resources.collect { it.id }
    }

    /**
     * Jenkins configuration
     */

    def jenkins(String name, String url, String user = '', String password = '') {
        ontrack.post(
                'extension/jenkins/configurations/create', [
                name    : name,
                url     : url,
                user    : user,
                password: password
        ])
    }

    List<String> getJenkins() {
        ontrack.get('extension/jenkins/configurations/descriptors').resources.collect { it.id }
    }

    /**
     * JIRA configuration
     */

    def jira(String name, String url, String user = '', String password = '') {
        ontrack.post(
                'extension/jira/configurations/create', [
                name    : name,
                url     : url,
                user    : user,
                password: password
        ])
    }

    List<String> getJira() {
        ontrack.get('extension/jira/configurations/descriptors').resources.collect { it.id }
    }

    /**
     * Artifactory configuration
     */

    def artifactory(String name, String url, String user = '', String password = '') {
        ontrack.post(
                'extension/artifactory/configurations/create', [
                name    : name,
                url     : url,
                user    : user,
                password: password
        ])
    }

    List<String> getArtifactory() {
        ontrack.get('extension/artifactory/configurations/descriptors').resources.collect { it.id }
    }

    /**
     * Predefined validation stamps
     */

    List<PredefinedValidationStamp> getPredefinedValidationStamps() {
        ontrack.get('admin/predefinedValidationStamps').resources.collect {
            new PredefinedValidationStamp(
                    ontrack,
                    it
            )
        }
    }

    PredefinedValidationStamp predefinedValidationStamp(String name, String description = '', boolean getIfExists = false) {
        def vs = predefinedValidationStamps.find { it.name == name }
        if (vs) {
            if (getIfExists) {
                new PredefinedValidationStamp(
                        ontrack,
                        ontrack.get(vs.link('self'))
                )
            } else {
                throw new ObjectAlreadyExistsException("Predefined validation stamp ${name} already exists.")
            }
        } else {
            new PredefinedValidationStamp(
                    ontrack,
                    ontrack.post(ontrack.get('admin/predefinedValidationStamps')._create, [
                            name       : name,
                            description: description
                    ])
            )
        }
    }

    PredefinedValidationStamp predefinedValidationStamp(String name, String description = '', boolean getIfExists = false, Closure closure) {
        def vs = predefinedValidationStamp(name, description, getIfExists)
        vs(closure)
        vs
    }

    /**
     * Predefined promotion levels
     */

    List<PredefinedPromotionLevel> getPredefinedPromotionLevels() {
        ontrack.get('admin/predefinedPromotionLevels').resources.collect {
            new PredefinedPromotionLevel(
                    ontrack,
                    it
            )
        }
    }

    PredefinedPromotionLevel predefinedPromotionLevel(String name, String description = '', boolean getIfExists = false) {
        def pl = predefinedPromotionLevels.find { it.name == name }
        if (pl) {
            if (getIfExists) {
                new PredefinedPromotionLevel(
                        ontrack,
                        ontrack.get(pl.link('self'))
                )
            } else {
                throw new ObjectAlreadyExistsException("Predefined promotion level ${name} already exists.")
            }
        } else {
            new PredefinedPromotionLevel(
                    ontrack,
                    ontrack.post(ontrack.get('admin/predefinedPromotionLevels')._create, [
                            name       : name,
                            description: description
                    ])
            )
        }
    }

    PredefinedPromotionLevel predefinedPromotionLevel(String name, String description = '', boolean getIfExists = false, Closure closure) {
        def vs = predefinedPromotionLevel(name, description, getIfExists)
        vs(closure)
        vs
    }

    /**
     * LDAP settings
     */

    @DSLMethod("Gets the global LDAP settings")
    LDAPSettings getLdapSettings() {
        def json = ontrack.get('settings/ldap').data
        return new LDAPSettings(json as Map)
    }

    @DSLMethod("Sets the global LDAP settings")
    def setLdapSettings(LDAPSettings settings) {
        ontrack.put('settings/ldap', settings)
    }
}
