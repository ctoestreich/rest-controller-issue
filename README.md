Two controllers that extend the RestfulController and override the save method.

By actually calling request.JSON.foo in the controller action, it stops the subsequent binding to the request.

Works
``` bash
curl -X POST -H "Content-Type: application/json" -H "Cache-Control: no-cache" -H "Postman-Token: c48c2524-a7a0-c098-2b78-2225e0d9ab7a" -d '{
    "name": "person name"
}' "http://localhost:8080/persons?foo=foo"
```


Does Not Work
``` bash
curl -X POST -H "Content-Type: application/json" -H "Cache-Control: no-cache" -H "Postman-Token: 67cfa64e-5b9e-8bf7-3a5c-3e8a1b104a31" -d '{
    "name": "place name"
}' "http://localhost:8080/places?foo=foo"
```

The addition of this line `println 'FOO=' + request.JSON.foo` causes it to break

```
 @Transactional
    def save() {
        if(handleReadOnly()) {
            return
        }

        println 'FOO=' + request.JSON.foo

        def instance = createResource()

        instance.validate()
        if (instance.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond instance.errors, view:'create' // STATUS CODE 422
            return
        }

        saveResource instance

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: "${resourceName}.label".toString(), default: resourceClassName), instance.id])
                redirect instance
            }
            '*' {
                response.addHeader(HttpHeaders.LOCATION,
                        grailsLinkGenerator.link( resource: this.controllerName, action: 'show',id: instance.id, absolute: true,
                                namespace: hasProperty('namespace') ? this.namespace : null ))
                respond instance, [status: CREATED, view:'show']
            }
        }
    }
```