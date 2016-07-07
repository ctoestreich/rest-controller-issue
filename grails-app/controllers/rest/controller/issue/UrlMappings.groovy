package rest.controller.issue

class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?" {
            constraints {
                // apply constraints here
            }
        }

        "/places"(resources: 'place')
        "/persons"(resources: 'person')

        "/"(view: "/index")
        "500"(view: '/error')
        "404"(view: '/notFound')
    }
}
